
public class Predictor9999 extends Predictor {
	public Table PHT;
	public Table BHR;                    // Trying to implement tournament predictor using Bimodal and Pag
	public Table selector;
	public int bimodalRate = 0;
	public int pagRate = 0;

	public Predictor9999() {
		PHT = new Table((int) Math.pow(2, 12), 2);       // PHT Table has total 2^12 entries with 2 bit saturating counter
		BHR = new Table((int) Math.pow(2, 8), 4);        // BHR table has total 2^8 entries with 4 bits per entry
		selector = new Table(1, 2);                      // Simple 2 bit saturating counter to select any one of the predictor
	}


	public void Train(long address, boolean outcome, boolean predict) {
		boolean result1 = predictForBimodal(address);
		boolean result2 = predictForPAg(address);
		trainBimodal(address, outcome, predict);
		trainPAg(address, outcome, predict);
		trainSelector(outcome, result1, result2);
		predictionAccuracy(result1, result2, outcome);
		return;
	}

	public boolean predict(long address){
		boolean result1 = predictForBimodal(address);
		boolean result2 = predictForPAg(address);
		int pred = selector.getInteger(0, 0, 1);
		if(bimodalRate > pagRate){
			return result1;
		}
		else if(bimodalRate < pagRate && pred <= (1<<2)-3){
			return result2;
		}
		else{
			return result2;
		}
	}

	public void trainSelector(boolean outcome, boolean result1, boolean result2){
		int pred = selector.getInteger(0, 0, 1);
		if(outcome == result1 && pred >= ((1<<2)-3)){
			selector.setInteger(0, 0, 1, --pred);
		}
		else if(outcome == result2 && pred <= ((1<<2)-2)){
			selector.setInteger(0, 0, 1, ++pred);
		}
	}

	public void predictionAccuracy(boolean result1, boolean result2, boolean outcome){
		if (result1 == outcome && result2 != outcome){
			bimodalRate++;
		}
		else if(result2 == outcome && result1 != outcome){
			pagRate++;
		}
	}

	public boolean predictForBimodal(long address){
		int maskBits = (1<<12)-1;
		int PHTIndex = (int) address&maskBits;
		int pred = PHT.getInteger(PHTIndex, 0, 1);
		if(pred < ((1<<2)-2)){
			return false;
		}
		else{
			return true;
		}
	}

	public void trainBimodal(long address, boolean outcome, boolean predict){
		int maskBits = (1<<12)-1;
		int PHTIndex = (int) address&maskBits;
		int pred = PHT.getInteger(PHTIndex, 0, 1);
		if(outcome == true){
			if(pred <= ((1<<2)-2)){
				PHT.setInteger(PHTIndex, 0, 1, ++pred);
			}
		}
		else{
			if(pred >= ((1<<2)-3)){
				PHT.setInteger(PHTIndex, 0, 1, --pred);
			}
		}
	}

	public boolean predictForPAg(long address){
		int maskBits1 = (1<<8)-1;
		int BHRIndex = (int) address&maskBits1;
		int BHRValue = BHR.getInteger(BHRIndex, 0, 3);
		int maskBits2 = (1<<12)-1;
		int PHTIndex = (BHRValue&maskBits2)^((int) (address&maskBits2));        // Taking XOR of BHR and PC
		int pred = PHT.getInteger(PHTIndex, 0, 1);
		if(pred < (1<<2)-2){
			return false;
		}
		else{
			return true;
		}
	}

	public void trainPAg(long address, boolean outcome, boolean predict){
		int maskBits1 = (1<<8)-1;
		int BHRIndex = (int) address&maskBits1;
		int BHRValue = BHR.getInteger(BHRIndex, 0, 3);
		int maskBits2 = (1<<12)-1;
		int PHTIndex = (int) (BHRValue&maskBits2)^((int) (address&maskBits2));        // Taking XOR of BHR and PC
		int pred = PHT.getInteger(PHTIndex, 0, 1);
		if(outcome == true){
			if (pred <= ((1<<2)-2)){
				PHT.setInteger(PHTIndex, 0, 1, ++pred);
			}
			int result = BHRValue>>1;
			BHR.setInteger(BHRIndex, 0, 3, result);
			BHR.setBit(BHRIndex, 0, true);
		}
		else{
			if(pred >= ((1<<2)-3)){
				PHT.setInteger(PHTIndex, 0, 1, --pred);
			}
			int result = BHRValue>>1;
			BHR.setInteger(BHRIndex, 0, 3, result);
			BHR.setBit(BHRIndex, 0, false);
		}
	}

}
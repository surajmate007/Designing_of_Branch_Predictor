
public class Predictor6400 extends Predictor {
	public Table PHT;
	public Table satCounter;

	public Predictor6400() {
		PHT = new Table((int) Math.pow(2, 10), 5);
		satCounter = new Table((int) Math.pow(2, 9), 2);
	}

	public void Train(long address, boolean outcome, boolean predict) {
		int maskBits1 = (1<<10)-1;
		int PHTIndex = (int) address&maskBits1;
		int pattern = PHT.getInteger(PHTIndex, 0, 4);
		int maskBits2 = (1<<9)-1;
		int index1 = (int) address&maskBits2;
		int index2 = (int) pattern&maskBits2;
		int index = index1^index2;
		int pred = satCounter.getInteger(index, 0, 1);
		if(outcome == true){
			if(pred <= (1<<2)-2){
				satCounter.setInteger(index, 0, 1, ++pred);
			}
			int pattHistory = pattern>>1;
			PHT.setInteger(PHTIndex, 0, 4, pattHistory);
			PHT.setBit(PHTIndex, 0, true);
		}else{
			if(pred >= (1<<2)-3){
				satCounter.setInteger(index, 0, 1, --pred);
			}
			int pattHistory = pattern>>1;
			PHT.setInteger(PHTIndex, 0, 4, pattHistory);
			PHT.setBit(PHTIndex, 0, false);
		}
	}

	public boolean predict(long address){
		int maskBits1 = (1<<10)-1;
		int PHTIndex = (int) address&maskBits1;
		int pattern = PHT.getInteger(PHTIndex, 0, 4);
		int maskBits2 = (1<<9)-1;
		int index1 = (int) address&maskBits2;
		int index2 = (int) pattern&maskBits2;
		int index = index1^index2;
		int pred = satCounter.getInteger(index, 0, 1);
		if(pred < (1<<2)-2){
			return false;
		}else{
			return true;
		}
	}
}
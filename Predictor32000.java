
public class Predictor32000 extends Predictor {
	public Table level1;
	public Table level2;
	public Table level3;

	public Predictor32000(){
		level1 = new Table((int) Math.pow(2, 10), 7);
		level2 = new Table((int) Math.pow(2, 12), 2);
		level3 = new Table((int) Math.pow(2, 14), 1);
	}

	public void Train(long address, boolean outcome, boolean predict) {
		int maskBits1 = (1<<10)-1;
		int level1Index = (int) address&maskBits1;
		int level1Output = level1.getInteger(level1Index, 0, 6);
		int maskBits2 = (1<<12)-1;
		int level2Index = (int) (address&maskBits2)^(level1Output&maskBits2);          // XOR of level1 output and PC bits
		int level2Output = level2.getInteger(level2Index, 0, 1);
		int maskBits3 = (1<<14)-1;
		int level3Index = (int) (address&maskBits3)^(level2Output&maskBits3);          // XOR of level2 output and PC bits
		boolean level3Output = level3.getBit(level3Index, 0);

		if(outcome == true){
			if(level3Output == false){
				level3.setBit(level3Index, 0, true);
			}
			if(level2Output <= ((1<<2)-2)){
				level2.setInteger(level2Index, 0, 1, ++level2Output);
			}
			level1.setInteger(level1Index, 0, 6, level1Output>>1);
			level1.setBit(level1Index, 0, true);
		}

		else{
			if(level3Output == true){
				level3.setBit(level3Index, 0, false);
			}
			if(level2Output >= ((1<<2)-3)){
				level2.setInteger(level2Index, 0, 1, --level2Output);
			}
			level1.setInteger(level1Index, 0, 6, level1Output>>1);
			level1.setBit(level1Index, 0, false);
		}
	}

	public boolean predict(long address){
		int maskBits1 = (1<<10)-1;
		int level1Index = (int) address&maskBits1;
		int level1Output = level1.getInteger(level1Index, 0, 6);
		int maskBits2 = (1<<12)-1;
		int level2Index = (int) (address&maskBits2)^(level1Output&maskBits2);          // XOR of level1 output and PC bits
		int level2Output = level2.getInteger(level2Index, 0, 1);
		int maskBits3 = (1<<14)-1;
		int level3Index = (int) (address&maskBits3)^(level2Output&maskBits3);          // XOR of level2 output and PC bits
		boolean pred = level3.getBit(level3Index, 0);
		if(pred == false){
			return false;
		}else{
			return true;
		}
	}

}
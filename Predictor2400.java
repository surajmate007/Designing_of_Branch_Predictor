public class Predictor2400 extends Predictor {
	public Table level1;
	public Table level2;

	public Predictor2400() {
		level1 = new Table((int) Math.pow(2, 5), 10);                       // Table is pointed by PC with 2^6 entries and each entry has 7 bit BHR
		level2 = new Table((int) Math.pow(2, 10), 2);                       // Table pointer by 7 BHR bits having 2^7 entries and each entry
	}

	public void Train(long address, boolean outcome, boolean predict) {
		int maskBits1 = (1<<5)-1;
		int nBitIndex = (int) (address&maskBits1);
		int BHRValue = level1.getInteger(nBitIndex, 0, 9);
		int maskBits2 = (1<<10)-1;
		BHRValue = BHRValue&maskBits2;                                     // BHR is also a 10 bit register
		int pcValue = (int) address&maskBits2;                             // 10 bits of PC are taken out
		int XORaddress = BHRValue^pcValue;                                 // both values are XORed to get the unique indexing
		XORaddress = XORaddress&((1<<10)-1);
		int pred = level2.getInteger(XORaddress, 0, 1);                    // predictor value (ie 00 to 11) is taken out
		if(outcome == true){
			if(pred <= ((1<<2)-2)){
				level2.setInteger(XORaddress, 0, 1, ++pred);
			}
			int result = BHRValue>>1;
			level1.setInteger(nBitIndex, 0, 9, result);                    // right shifting the BHR bits
			level1.setBit(nBitIndex, 0, true);                             // setting first bit as 1 since branch is taken
		}
		else{
			if(pred >= ((1<<2)-3)){
				level2.setInteger(XORaddress, 0, 1, --pred);
			}
			int result = BHRValue>>1;                                  // when branch is not taken the just shifting the result by 1 bit
			level1.setInteger(nBitIndex, 0, 9, result);
			level1.setBit(nBitIndex, 0, false);
		}
	}

	public boolean predict(long address) {
		int maskBits1 = (1<<5)-1;                                          // helps to keep the index within the range
		int nBitIndex = (int) (address&maskBits1);
		int BHRValue = level1.getInteger(nBitIndex, 0, 9);                
		int maskBits2 = (1<<10)-1;
		BHRValue = BHRValue&maskBits2;
		int pcValue = (int) address&maskBits2;
		int XORaddress = BHRValue^pcValue;                                 // taking bitwise XOR of bhr and PC
		XORaddress = XORaddress&((1<<10)-1);
		int pred = level2.getInteger(XORaddress, 0, 1);
		if(pred < ((1<<2)-2)){
			return false;
		}
		else{
			return true;
		}
	}
}
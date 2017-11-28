package com.tiesdb.lib.merkletree;

abstract public class BinaryTrieNodeBase {
	long prefix0;
	long prefix1;
	byte offsetStart; //Offset to start from the start (0 - 127)
	byte offsetEnd; //Offset to end from the end (negative 0 - -128)
	
	boolean hashIsValid;
	
	TrieProperties properties;
	byte[] hash;
	
	public BinaryTrieNodeBase(TrieProperties properties, long prefix0, long prefix1, byte offsetStart, byte offsetEnd) {
		this.properties = properties;
		this.prefix0 = prefix0;
		this.prefix1 = prefix1;
		setOffsets(offsetStart, offsetEnd);
	}
	
	boolean compare(long id0, long id1) {
		long mask0 = makeMask(offsetStart, -64 < offsetEnd ? 0 : offsetEnd + 64);
		long mask1 = makeMask(offsetStart < 64 ? 0 : offsetStart-64, offsetEnd);
		return ((prefix0 & mask0) == (id0 & mask0) && (prefix1 & mask1) == (id1 & mask1));
	}
	
	public boolean isEnd() {
		return offsetEnd == 0;
	}
	
	/**
	 * Checks if the next bit after offsetEnd position is 1
	 *  
	 * @param id0
	 * @param id1
	 * @return 
	 */
	public boolean isNext1(long id0, long id1) {
		long mask;
		if(-64 < offsetEnd) {
			mask = 1L << (64 + offsetEnd);
			return (mask & id1) != 0;
		} else {
			mask = 1L << (128 + offsetEnd);
			return (mask & id0) != 0;
		}
	}
	
	protected static long makeMask(int start, int end) {
		long mask = -1;
		if(start >= 64 || -end >= 64)
			return 0;
		mask = -1L << start;
		mask &= (-1L >>> -end);
		return mask;
	}
	
	byte findCommon(long id0, long id1) {
		int i;
		for(i=this.offsetStart; i<128; ++i) {
			long mask = 1L << i;
			long px = i < 64 ? this.prefix0 : this.prefix1;
			long id = i < 64 ? id0 : id1;
			if((mask & px) != (mask & id)) {
				break;
			}
		}
		
		if(i >= 128) {
			//The leafs are equivalent
			return -1;
		}
		
		return (byte)i;
	}
	
	void setOffsets(byte offsetStart, byte offsetEnd) {
		assert(offsetStart >= this.offsetStart); //Offsets can only shrink
		assert(offsetEnd <= this.offsetEnd);
		
		this.offsetStart = offsetStart;
		this.offsetEnd = offsetEnd;
	}
	
	boolean isHashValid() {
		return hash != null && hashIsValid;
	}
	
	byte[] ensureHash() {
		if(hash == null)
			hash = new byte[properties.hash.getDigestSize()];
		return hash;	
	}
	
	abstract public void recomputeHash();
}

package com.tiesdb.lib.merkletree;

import com.tiesdb.lib.crypto.digest.api.Digest;

public class BinaryTrieLeaf extends BinaryTrieNodeBase{
	
	BinaryTrieLeaf(TrieProperties properties, long id0, long id1, byte[] data) {
		super(properties, id0, id1, (byte)0, (byte)0);
		hashData(data);
	}
	
	BinaryTrieLeaf(TrieProperties properties, long id0, long id1, byte[] data, byte start) {
		super(properties, id0, id1, start, (byte)0); //Leafs always have offsetEnd == 0
		hashData(data);
	}
	
	void hashData(byte[] data, byte[] out) {
		Digest digest = properties.hash;
		digest.reset();
		digest.update((byte)0x0);
		digest.update(data, 0, data.length);
		
		digest.doFinal(out, 0);
	}

	void hashData(byte[] data) {
		hashData(data, ensureHash());
		hashIsValid = true;
	}
	
	byte[] hashDataReturn(byte[] data) {
		byte[] out = new byte[properties.hash.getDigestSize()];
		hashData(data, out);
		return out;
	}

	@Override
	public void recomputeHash() {
		assert(isHashValid()); //The hash of leaf node can not be invalidated
	}
	
}

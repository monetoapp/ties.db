package com.tiesdb.protocol.v0;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tiesdb.protocol.TiesDBProtocol;
import com.tiesdb.protocol.TiesDBProtocolManager;
import com.tiesdb.protocol.v0.impl.TiesDBProtocolV0Impl;

@DisplayName("TiesDBProtocol version 0 Miscelaneous Test")
public class TiesDBProtocolV0_MiscTest {

	@Test
	@DisplayName("Protocol Service Loading")
	void testProtocolServiceLoading() {
		List<TiesDBProtocol> protocols = TiesDBProtocolManager.loadProtocols();
		assertFalse(protocols.isEmpty(), "No Protocols found");
		assertEquals(1, protocols.size());
		assertEquals(new TiesDBProtocolV0Impl().getVersion(), protocols.get(0).getVersion());
	}

}

package com.kain.hap.proxy.srp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.kain.hap.proxy.tlv.type.PublicKey;

class SrpCommunicationTest {
	
	
	@Test
	@Disabled
	void testServerSession_1024() {
		AccessorySession session = new AccessorySession(Group.G_1024_BIT, SrpTestVector_1024.I, SrpTestVector_1024.P,  SrpTestVector_1024.s,  SrpTestVector_1024.b);
		assertThat(session.getPublicKey().getKey()).isEqualTo(SrpTestVector_1024.B);
		
		byte[] M2 = session.respond(new PublicKey(SrpTestVector_1024.A), new byte[0]);
		assertThat(M2).isEqualTo(M2);
	}

	@Test
	void testSrp_3072() {
		AccessorySession server = new AccessorySession(Group.G_3072_BIT, SrpTestVector_3072.I,  SrpTestVector_3072.P,  SrpTestVector_3072.s,  SrpTestVector_3072.b);
		assertThat(server.getPublicKey().getKey()).isEqualTo(SrpTestVector_3072.B);
		
		DeviceSession device = new DeviceSession(Group.G_3072_BIT, SrpTestVector_3072.a, SrpTestVector_3072.I, SrpTestVector_3072.P);
		assertThat(device.getPublicKey().getKey()).isEqualTo(SrpTestVector_3072.A);
		
		byte[] M1 = device.respond(new PublicKey(SrpTestVector_3072.B), SrpTestVector_3072.s);
		byte[] M2 = server.respond(new PublicKey(SrpTestVector_3072.A), M1);
		
		boolean result = device.verify(M2);
		assertThat(result).isTrue();
	}
	
	
	
}

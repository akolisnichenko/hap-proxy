package com.kain.hap.proxy.srp;

import static com.kain.hap.proxy.service.SrpCalculation.hash;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.kain.hap.proxy.service.AccessorySession;
import com.kain.hap.proxy.service.DeviceSession;
import com.kain.hap.proxy.tlv.type.Proof;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.SrpPublicKey;

public class ServerSession {
	
	
	@Test
	void hashFunction() {
		byte[] hash =  hash(SrpTestVector_1024.s, hash(SrpTestVector_1024.I, ":", SrpTestVector_1024.P));
		assertThat(hash).isEqualTo(SrpTestVector_1024.x);
	}
	
	@Test
	@Disabled
	void testServerSession_1024() {
		AccessorySession session = new AccessorySession(Group.G_1024_BIT, SrpTestVector_1024.I, SrpTestVector_1024.P,  SrpTestVector_1024.s,  SrpTestVector_1024.b);
		assertThat(session.getPublicKey()).isEqualTo(SrpTestVector_1024.B);
		
		byte[] M2 = session.respond(new SrpPublicKey(SrpTestVector_1024.A), new Proof(new byte[0]));
		assertThat(M2).isEqualTo(M2);
	}

	@Test
	void testSrp_3072() {
		AccessorySession server = new AccessorySession(Group.G_3072_BIT, SrpTestVector_3072.I,  SrpTestVector_3072.P,  SrpTestVector_3072.s,  SrpTestVector_3072.b);
		assertThat(server.getPublicKey()).isEqualTo(SrpTestVector_3072.B);
		
		DeviceSession device = new DeviceSession(Group.G_3072_BIT, SrpTestVector_3072.a, SrpTestVector_3072.I, SrpTestVector_3072.P);
		assertThat(device.getPublicKey()).isEqualTo(SrpTestVector_3072.A);
		
		byte[] M1 = device.respond(new SrpPublicKey(SrpTestVector_3072.B), new Salt(SrpTestVector_3072.s));
		assertThat(M1).isEqualTo(M1);
		
		byte[] M2 = server.respond(new SrpPublicKey(SrpTestVector_3072.A), new Proof(M1));
		
		device.verify(M2);
		//assertThat(M2).isEqualTo(M2);
	}
	
	
	
}

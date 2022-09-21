package com.kain.hap.proxy.srp;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Group {
	G_3072_BIT(toBigInt( new int[] { 0xFFFFFFFF, 0xFFFFFFFF, 0xC90FDAA2, 0x2168C234, 0xC4C6628B, 0x80DC1CD1, 0x29024E08,
		0x8A67CC74, 0x020BBEA6, 0x3B139B22, 0x514A0879, 0x8E3404DD, 0xEF9519B3, 0xCD3A431B,
		0x302B0A6D, 0xF25F1437, 0x4FE1356D, 0x6D51C245, 0xE485B576, 0x625E7EC6, 0xF44C42E9,
		0xA637ED6B, 0x0BFF5CB6, 0xF406B7ED, 0xEE386BFB, 0x5A899FA5, 0xAE9F2411, 0x7C4B1FE6,
		0x49286651, 0xECE45B3D, 0xC2007CB8, 0xA163BF05, 0x98DA4836, 0x1C55D39A, 0x69163FA8,
		0xFD24CF5F, 0x83655D23, 0xDCA3AD96, 0x1C62F356, 0x208552BB, 0x9ED52907, 0x7096966D,
		0x670C354E, 0x4ABC9804, 0xF1746C08, 0xCA18217C, 0x32905E46, 0x2E36CE3B, 0xE39E772C,
		0x180E8603, 0x9B2783A2, 0xEC07A28F, 0xB5C55DF0, 0x6F4C52C9, 0xDE2BCBF6, 0x95581718,
		0x3995497C, 0xEA956AE5, 0x15D22618, 0x98FA0510, 0x15728E5A, 0x8AAAC42D, 0xAD33170D,
		0x04507A33, 0xA85521AB, 0xDF1CBA64, 0xECFB8504, 0x58DBEF0A, 0x8AEA7157, 0x5D060C7D,
		0xB3970F85, 0xA6E1E4C7, 0xABF5AE8C, 0xDB0933D7, 0x1E8C94E0, 0x4A25619D, 0xCEE3D226,
		0x1AD2EE6B, 0xF12FFA06, 0xD98A0864, 0xD8760273, 0x3EC86A64, 0x521F2B18, 0x177B200C,
		0xBBE11757, 0x7A615D6C, 0x770988C0, 0xBAD946E2, 0x08E24FA0, 0x74E5AB31, 0x43DB5BFC,
		0xE0FD108E, 0x4B82D120, 0xA93AD2CA, 0xFFFFFFFF, 0xFFFFFFFF}),
		BigInteger.valueOf(5l)
			);
	
	private final BigInteger prime;
	private final BigInteger generator;
	
	private static BigInteger toBigInt(int[] primeArray) {
		ByteBuffer bb = ByteBuffer.allocate(primeArray.length * 4);
		for (int i = 0; i < primeArray.length; i++) {
			bb.putInt(primeArray[i]);
		}
		return new BigInteger(1, bb.array());
	}
	
	public BigInteger getN() {
		return prime;
	}
	public byte[] getNAsArr() {
		return prime.toByteArray();
	}
	
	public BigInteger getG() {
		return generator;
	}
	public byte[] getGAsArr() {
		return generator.toByteArray();
	}
	

}

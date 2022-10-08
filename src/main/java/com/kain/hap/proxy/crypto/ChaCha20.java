package com.kain.hap.proxy.crypto;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

import com.google.common.primitives.Bytes;

public class ChaCha20 {
	
	private static final int[] constant = { 0x61707865, 0x3320646e, 0x79622d32, 0x6b206574 };
	private static final int[][] rounds = new int[][] { { 0, 4, 8, 12 }, { 1, 5, 9, 13 }, { 2, 6, 10, 14 }, { 3, 7, 11, 15 },
			{ 0, 5, 10, 15 }, { 1, 6, 11, 12 }, { 2, 7, 8, 13 }, { 3, 4, 9, 14 } };
	private final int[] state;
	
	private static final long[] polyRMask = new long[] {0x0ffffffc0ffffffcL, 0x0ffffffc0fffffffL};
	

	public static void main(String[] args) {
		byte[] key = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 
		      0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f};
		byte[] nonce = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x4a, 0x00, 0x00, 0x00, 0x00};
		int count = 1; 
		String plainText = "Ladies and Gentlemen of the class of '99: If I could offer you only one tip for the future, sunscreen would be it.";
		
		byte[] encrypted = ChaCha20.encrypt(key, nonce, count, plainText.getBytes());
		byte[] plain2 = ChaCha20.encrypt(key, nonce, count, encrypted);
		
		System.out.println("encripted:\t"+ new String(encrypted));
		System.out.println("normal:\t" + new String(plain2));
		
		// poly1305
		ByteBuffer bb = ByteBuffer.allocate(32);
		long[] p1305keyInt = new long[]{ 0x85d6be7857556d33L, 0x7f4452fe42d506a8L, 0x0103808afb0db2fdL, 0x4abff6af4149f51bL};
		for (long l : p1305keyInt) {
			bb.putLong(l);
		}
		byte[] oneTimeKey = bb.array();
		byte[] message = "Cryptographic Forum Research Group".getBytes();
		byte[] poly1305Result = ChaCha20.poly1305mac(oneTimeKey, message);
		System.out.println(new String(poly1305Result));
		
		long[] keyAEADLong = new long[] {0x8081828384858687L,  0x88898a8b8c8d8e8fL,
				  0x9091929394959697L, 0x98999a9b9c9d9e9fL};
		ByteBuffer keyAEADBB = ByteBuffer.allocate(32);
		for (long l : keyAEADLong) {
			keyAEADBB.putLong(l);
		}
		
		byte[] keyAEAD = keyAEADBB.array();
		byte[] aad = new byte[] {0x50, 0x51, 0x52, 0x53, (byte)0xc0, (byte)0xc1, (byte)0xc2, (byte)0xc3, (byte)0xc4, (byte)0xc5, (byte)0xc6, (byte)0xc7};
		byte[] IV = new byte[] {0x40, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47};
		byte[] fixedCP = new byte[] {0x07, 0x00, 0x00, 0x00};
		
		AEADResult result = aead(keyAEAD, IV, fixedCP, plainText.getBytes(), aad);
		System.out.println(" Result AEAD"+result);
		
		// shitcoding, sorry
				long[] keyOtkLong = new long[] {0x0000000000000000L, 0x0000000000000000L,
				0x36e5f6b5c5e06070L, 0xf0efca96227a863eL};
		ByteBuffer keyOtkBB = ByteBuffer.allocate(32);
		for (long l : keyOtkLong) {
			keyOtkBB.putLong(l);
		}
		byte[] keyOtk = keyOtkBB.array();
		String plainMessage = "Any submission to the IETF intended by the Contributor for publication as all or part of an IETF Internet-Draft or RFC and any statement made within the context of an IETF activity is considered an \"IETF Contribution\". Such statements include oral statements in IETF sessions, as well as written and electronic communications made at any time or place, which are addressed to";
		byte[] byteMessage = plainMessage.getBytes();
		byte[] tv2 = poly1305mac(keyOtk, byteMessage);
		System.out.println(" Result AEAD2"+tv2);
	}

	private int[] quarterRound(int[] words) {
		words[0] += words[1];
		words[3] ^= words[0];
		words[3] = Integer.rotateLeft(words[3], 16);

		words[2] += words[3];
		words[1] ^= words[2];
		words[1] = Integer.rotateLeft(words[1], 12);

		words[0] += words[1];
		words[3] ^= words[0];
		words[3] = Integer.rotateLeft(words[3], 8);

		words[2] += words[3];
		words[1] ^= words[2];
		words[1] = Integer.rotateLeft(words[1], 7);
		return words;
	}

	public static byte[] encrypt(byte[] key, byte[] nonce, int count, byte[] plainText) {
		int blockNum = plainText.length/64;
		byte[] fullKeyStream = new byte[0];
		for(int i = 0; i <= blockNum; i++) {
			byte[] keyStream = new ChaCha20(key, nonce, count+i).block();
			fullKeyStream = Bytes.concat(fullKeyStream, keyStream);
		}
		byte[] result = xorArrays(fullKeyStream, plainText);
		return result;
	}
	
	private static byte[] xorArrays(byte[] key, byte[] part) {
		byte[] result = new byte[part.length];
		for (int i = 0; i < part.length; i++) {
			result[i] = (byte)(part[i] ^ key[i]);
		}
		return result;
	}
	
	private ChaCha20(byte[] key, byte[] nonce, int count) {
		
		// remove limitation on 32 bytes
/*		if (key.length != 32) {
			throw new RuntimeException("Wrong key");
		}
*/		
		IntBuffer ib = ByteBuffer.wrap(key).order(java.nio.ByteOrder.LITTLE_ENDIAN).asIntBuffer();
		int[] keyArr = new int[ib.limit()];
		ib.get(keyArr);
		
		
		//FIXME: possible problem - uses the 64-bit nonce option where the first 32 bits of 96-bit nonce are 0.
		// What is the first 32 bit
		if(nonce.length < 12) {
			nonce = Bytes.concat(new byte[12 - nonce.length], nonce);
		}
		
		ib = ByteBuffer.wrap(nonce).order(java.nio.ByteOrder.LITTLE_ENDIAN).asIntBuffer();
		int[] nonceArr = new int[ib.limit()];
		ib.get(nonceArr);

		state = new int[] { constant[0], constant[1], constant[2], constant[3], keyArr[0], keyArr[1], keyArr[2],
				keyArr[3], keyArr[4], keyArr[5], keyArr[6], keyArr[7], count, nonceArr[0], nonceArr[1], nonceArr[2] };

	}

	

	private void quarterRoundChaChaState(int[] idx) {
		if (idx.length != 4 && state.length != 16) {
			throw new RuntimeException();
		}
		state[idx[0]] += state[idx[1]];
		state[idx[3]] ^= state[idx[0]];
		state[idx[3]] = Integer.rotateLeft(state[idx[3]], 16);

		state[idx[2]] += state[idx[3]];
		state[idx[1]] ^= state[idx[2]];
		state[idx[1]] = Integer.rotateLeft(state[idx[1]], 12);

		state[idx[0]] += state[idx[1]];
		state[idx[3]] ^= state[idx[0]];
		state[idx[3]] = Integer.rotateLeft(state[idx[3]], 8);

		state[idx[2]] += state[idx[3]];
		state[idx[1]] ^= state[idx[2]];
		state[idx[1]] = Integer.rotateLeft(state[idx[1]], 7);
	}

	private byte[] block() {
		int[] workingState = Arrays.copyOf(state, state.length);
		for (int i = 0; i < 10; i++) {
			for (int r = 0; r < rounds.length; r++) {
				quarterRoundChaChaState(rounds[r]);
			}
		}
		for (int i = 0; i < state.length; i++) {
			state[i] += workingState[i];
		}
		return serialize(state);
	}

	private byte[] serialize(int[] array) {
		ByteBuffer bb = ByteBuffer.allocate(array.length * 4).order(ByteOrder.LITTLE_ENDIAN);
		bb.asIntBuffer().put(array);
		return bb.array();
	}
	

	public static byte[] poly1305mac(byte[] oneTimeKey, byte[] message) {
		ByteBuffer bb = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);
		bb.put(Arrays.copyOfRange(oneTimeKey, 0, 16));
		long[] dst = new long[2];
		dst[1] = bb.getLong(0);
		dst[0] = bb.getLong(8);
		byte[] rArr = clamp(dst);
		byte[] sArr = Arrays.copyOfRange(oneTimeKey, 16, 32);
		Bytes.reverse(sArr);
		
		BigInteger s = new BigInteger(1,sArr);
		BigInteger a = BigInteger.ZERO;
		BigInteger p = BigInteger.ONE.shiftLeft(130).subtract(BigInteger.valueOf(5l)) ;
		BigInteger r = new BigInteger(1, rArr);
		int i = 0;
		for (i = 0; i < message.length -16 ; i+=16) {
			byte[] part = Arrays.copyOfRange(message, i, i + 16);
			Bytes.reverse(part);
			byte[] n = ByteBuffer.allocate(17).order(ByteOrder.LITTLE_ENDIAN).put((byte)0x01).put(part).array();
			a =  a.add(new BigInteger(1, n));
			a = r.multiply(a);
			a = a.mod(p);
		}
		int diff = message.length - i;
		if(diff > 0) {
			byte[] part = Arrays.copyOfRange(message, message.length - diff, message.length);
			Bytes.reverse(part);
			byte[] n = ByteBuffer.allocate(diff + 1).order(ByteOrder.LITTLE_ENDIAN).put((byte)0x01).put(part).array();
			a =  a.add(new BigInteger(1, n));
			a = r.multiply(a);
			a = a.mod(p);
		}
		a = a.add(s);
		return numTo16LEBytes(a);
		//return a.toByteArray();
	}

	private static byte[] clamp(long[] r) {
		ByteBuffer bb = ByteBuffer.allocate(16);
		bb.putLong(r[0] & polyRMask[0]);
		bb.putLong(r[1] & polyRMask[1]);
		return bb.array();
	}
	
	// TODO: test
	public static byte[] poly1305Gen(byte[] key, byte[] nonce) {
		int count = 0;
		byte[] block = new ChaCha20(key, nonce, count).block();
		return Arrays.copyOfRange(block, 0, 32);
	}
	
	public static AEADResult aead(byte[] key, byte[] iv, byte[] constant, byte[] plainText, byte[] aad) {
		byte nonce[] = Bytes.concat(constant, iv);
		return  aead(key, nonce, plainText, aad);
	}
	
	
	// as encodeCiphertext
	public static AEADResult aead(byte[] key, byte[] nonce, byte[] plainText, byte[] aad) {
		byte[] otk = poly1305Gen(key, nonce);
		byte[] encrypt = encrypt(key, nonce, 1, plainText);
		byte[] macData = Bytes.concat(aad, pad16(aad.length));
		macData = Bytes.concat(macData, encrypt, pad16(encrypt.length));
		macData = Bytes.concat(macData, numTo4LEBytes(aad.length));
		macData = Bytes.concat(macData, numTo4LEBytes(encrypt.length));
		byte[] tag = poly1305mac(otk, macData);
		return new AEADResult(encrypt, tag);
	}
	
	public static byte[] decode(byte[] key, byte[] nonce, AEADResult result, byte[] aad) throws AuthTagCheckException{
		byte[] otk = poly1305Gen(key, nonce);
		byte[] macData = Bytes.concat(aad, pad16(aad.length));
		macData = Bytes.concat(macData, result.getEncrypted(), pad16(result.getEncrypted().length));
		macData = Bytes.concat(macData, numTo4LEBytes(aad.length));
		macData = Bytes.concat(macData, numTo4LEBytes(result.getEncrypted().length));
		byte[] tag = poly1305mac(otk, macData);
		if (Arrays.compare(tag, result.getTag()) != 0) {
			throw new AuthTagCheckException();
		}
		byte[] plain = encrypt(key, nonce, 1, result.getEncrypted());
		
		return plain;
	}
	
	private static byte[] pad16(int length) {
		int l = length % 16;
		return new byte[ l == 0 ? 0 : 16 - l ];
	}
	
	private static byte[] numTo16LEBytes(BigInteger bigInt) {
		byte[] result = bigInt.toByteArray();
		Bytes.reverse(result);
		return Arrays.copyOfRange(result, 0, 16);
	}
	
	
	private static byte[] numTo4LEBytes(int length) {
		byte[] n = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putInt(length).array();
		return n;
	}
	
}


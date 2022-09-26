package com.kain.hap.proxy.srp;

import java.nio.ByteBuffer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SrpTestVector_1024 {
	public static final String I = "alice";
	public static final String P = "password123";

	public static final byte[] s = toByteArray(new long[] { 0xBEB25379D1A8581EL, 0xB5A727673A2441EEL });

//		      N, g = <1024-bit parameters from Appendix A>

	public static final byte[] k = toByteArray(
			new int[] { 0x7556AA04, 0x5AEF2CDD, 0x07ABAF0F, 0x665C3E81, 0x8913186F });

	public static final byte[] x = toByteArray(
			new int[] { 0x94B7555A, 0xABE9127C, 0xC58CCF49, 0x93DB6CF8, 0x4D16C124 });

	public static final byte[] v = toByteArray(
			new long[] { 
					0x7E273DE8_696FFC4FL, 0x4E337D05_B4B375BEL, 0xB0DDE156_9E8FA00AL, 0x9886D812_9BADA1F1L,
					0x822223CA_1A605B53L, 0x0E379BA4_729FDC59L, 0xF105B478_7E5186F5L, 0xC671085A_1447B52AL,
					0x48CF1970_B4FB6F84L, 0x00BBF4CE_BFBB1681L, 0x52E08AB5_EA53D15CL, 0x1AFF87B2_B9DA6E04L,
					0xE058AD51_CC72BFC9L, 0x033B564E_26480D78L, 0xE955A5E2_9E7AB245L, 0xDB2BE315_E2099AFBL });
	public static final byte[] a = toByteArray(
			new long[] { 0x60975527_035CF2ADL, 0x1989806F_0407210BL, 0xC81EDC04_E2762A56L, 0xAFD529DD_DA2D4393L });
	public static final byte[] b = toByteArray(
			new long[] { 0xE487CB59_D31AC550L, 0x471E81F0_0F6928E0L, 0x1DDA08E9_74A004F4L, 0x9E61F5D1_05284D20L });

	public static final byte[] A = toByteArray(
			new long[] { 
					0x61D5E490_F6F1B795L, 0x47B0704C_436F523DL, 0xD0E560F0_C64115BBL, 0x72557EC4_4352E890L,
					0x3211C046_92272D8BL, 0x2D1A5358_A2CF1B6EL, 0x0BFCF99F_921530ECL, 0x8E393561_79EAE45EL,
					0x42BA92AE_ACED8251L, 0x71E1E8B9_AF6D9C03L, 0xE1327F44_BE087EF0L, 0x6530E69F_66615261L,
					0xEEF54073_CA11CF58L, 0x58F0EDFD_FE15EFEAL, 0xB349EF5D_76988A36L, 0x72FAC47B_0769447BL });

	public static final byte[] B = toByteArray(
			new long[] { 
					0xBD0C6151_2C692C0CL, 0xB6D041FA_01BB152DL, 0x4916A1E7_7AF46AE1L, 0x05393011_BAF38964L,
					0xDC46A067_0DD125B9L, 0x5A981652_236F99D9L, 0xB681CBF8_7837EC99L, 0x6C6DA044_53728610L,
					0xD0C6DDB5_8B318885L, 0xD7D82C7F_8DEB75CEL, 0x7BD4FBAA_37089E6FL, 0x9C6059F3_88838E7AL,
					0x00030B33_1EB76840L, 0x910440B1_B27AAEAEL, 0xEB4012B7_D7665238L, 0xA8E3FB00_4B117B58L });

	public static final byte[] u = toByteArray(
			new int[] { 0xCE38B959, 0x3487DA98, 0x554ED47D, 0x70A7AE5F, 0x462EF019 });

	public static final byte[] premasterSecret = toByteArray(
			new long[] { 
					0xB0DC82BA_BCF30674L, 0xAE450C02_87745E79L, 0x90A3381F_63B387AAL, 0xF271A10D_233861E3L,
					0x59B48220_F7C4693CL, 0x9AE12B0A_6F67809FL, 0x0876E2D0_13800D6CL, 0x41BB59B6_D5979B5CL,
					0x00A172B4_A2A5903AL, 0x0BDCAF8A_709585EBL, 0x2AFAFA8F_3499B200L, 0x210DCC1F_10EB3394L,
					0x3CD67FC8_8A2F39A4L, 0xBE5BEC4E_C0A3212DL, 0xC346D7E4_74B29EDEL, 0x8A469FFE_CA686E5AL });

	private static byte[] toByteArray(long[] data) {
		ByteBuffer resultBuffer = ByteBuffer.allocate(Long.BYTES * data.length);
		for (int i = 0; i < data.length; i++) {
			resultBuffer.putLong(data[i]);
		}
		return resultBuffer.array();
	}

	private static byte[] toByteArray(int[] data) {
		ByteBuffer resultBuffer = ByteBuffer.allocate(Integer.BYTES * data.length);
		for (int i = 0; i < data.length; i++) {
			resultBuffer.putInt(data[i]);
		}
		return resultBuffer.array();
	}
}

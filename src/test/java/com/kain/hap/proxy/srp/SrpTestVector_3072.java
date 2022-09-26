package com.kain.hap.proxy.srp;

import java.nio.ByteBuffer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SrpTestVector_3072 {
	public static final String I = "alice";
	public static final String P = "password123";

	public static final byte[] s = toByteArray(new long[] { 0xBEB25379_D1A8581EL, 0xB5A72767_3A2441EEL });

//		      N, g = <1024-bit parameters from Appendix A>

	public static final byte[] k = toByteArray(
			new int[] { 0x7556AA04, 0x5AEF2CDD, 0x07ABAF0F, 0x665C3E81, 0x8913186F });

	public static final byte[] x = toByteArray(
			new int[] { 0x94B7555A, 0xABE9127C, 0xC58CCF49, 0x93DB6CF8, 0x4D16C124 });

	public static final byte[] v = toByteArray(
			new long[] { 
					0x9B5E0617_01EA7AEBL, 0x39CF6E35_19655A85L, 0x3CF94C75_CAF2555EL, 0xF1FAF759_BB79CB47L,
					0x7014E04A_88D68FFCL, 0x05323891_D4C205B8L, 0xDE81C2F2_03D8FAD1L, 0xB24D2C10_9737F1BEL,
					0xBBD71F91_2447C4A0L, 0x3C26B9FA_D8EDB3E7L, 0x80778E30_2529ED1EL, 0xE138CCFC_36D4BA31L,
					0x3CC48B14_EA8C22A0L, 0x186B222E_655F2DF5L, 0x603FD75D_F76B3B08L, 0xFF895006_9ADD03A7L,
					0x54EE4AE8_8587CCE1L, 0xBFDE3679_4DBAE459L, 0x2B7B904F_442B041CL, 0xB17AEBAD_1E3AEBE3L,
					0xCBE99DE6_5F4BB1FAL, 0x00B0E7AF_06863DB5L, 0x3B02254E_C66E781EL, 0x3B62A821_2C86BEB0L,
					0xD50B5BA6_D0B478D8L, 0xC4E9BBCE_C2176532L, 0x6FBD1405_8D2BBDE2L, 0xC33045F0_3873E539L,
					0x48D78B79_4F0790E4L, 0x8C36AED6_E880F557L, 0x427B2FC0_6DB5E1E2L, 0xE1D7E661_AC482D18L,
					0xE528D729_5EF74372L, 0x95FF1A72_D4027717L, 0x13F16876_DD050AE5L, 0xB7AD53CC_B90855C9L,
					0x39566483_58ADFD96L, 0x6422F524_98732D68L, 0xD1D7FBEF_10D78034L, 0xAB8DCB6F_0FCF885CL,
					0xC2B2EA2C_3E6AC866L, 0x09EA058A_9DA8CC63L, 0x531DC915_414DF568L, 0xB09482DD_AC1954DEL,
					0xC7EB714F_6FF7D44CL, 0xD5B86F6B_D1158109L, 0x30637C01_D0F6013BL, 0xC9740FA2_C633BA89L });
	public static final byte[] a = toByteArray(
			new long[] { 0x60975527_035CF2ADL, 0x1989806F_0407210BL, 0xC81EDC04_E2762A56L, 0xAFD529DD_DA2D4393L });
	public static final byte[] b = toByteArray(
			new long[] { 0xE487CB59_D31AC550L, 0x471E81F0_0F6928E0L, 0x1DDA08E9_74A004F4L, 0x9E61F5D1_05284D20L });

	public static final byte[] A = toByteArray(
			new long[] { 
					0xFAB6F5D2_615D1E32L, 0x3512E799_1CC37443L, 0xF487DA60_4CA8C923L, 0x0FCB04E5_41DCE628L,
					0x0B27CA46_80B0374FL, 0x179DC3BD_C7553FE6L, 0x2459798C_701AD864L, 0xA91390A2_8C93B644L,
					0xADBF9C00_745B942BL, 0x79F9012A_21B9B787L, 0x82319D83_A1F83628L, 0x66FBD6F4_6BFC0DDBL,
					0x2E1AB6E4_B45A9906L, 0xB82E37F0_5D6F97F6L, 0xA3EB6E18_2079759CL, 0x4F684783_7B62321AL,
					0xC1B4FA68_641FCB4BL, 0xB98DD697_A0C73641L, 0x385F4BAB_25B79358L, 0x4CC39FC8_D48D4BD8L,
					0x67A9A3C1_0F8EA121L, 0x70268E34_FE3BBE6FL, 0xF89998D6_0DA2F3E4L, 0x283CBEC1_393D52AFL,
					0x724A5723_0C604E9FL, 0xBCE583D7_613E6BFFL, 0xD67596AD_121A8707L, 0xEEC46944_95703368L,
					0x6A155F64_4D5C5863L, 0xB48F61BD_BF19A53EL, 0xAB6DAD0A_186B8C15L, 0x2E5F5D8C_AD4B0EF8L,
					0xAA4EA500_8834C3CDL, 0x342E5E0F_167AD045L, 0x92CD8BD2_79639398L, 0xEF9E114D_FAAAB919L,
					0xE14E8509_89224DDDL, 0x98576D79_385D2210L, 0x902E9F9B_1F2D86CFL, 0xA47EE244_635465F7L,
					0x1058421A_0184BE51L, 0xDD10CC9D_079E6F16L, 0x04E7AA9B_7CF7883CL, 0x7D4CE12B_06EBE160L,
					0x81E23F27_A231D184L, 0x32D7D1BB_55C28AE2L, 0x1FFCF005_F57528D1L, 0x5A88881B_B3BBB7FEL});

	public static final byte[] B = toByteArray(
			new long[] { 
					0x40F57088_A482D4C7L, 0x733384FE_0D301FDDL, 0xCA9080AD_7D4F6FDFL, 0x09A01006_C3CB6D56L,
					0x2E41639A_E8FA21DEL, 0x3B5DBA75_85B27558L, 0x9BDB2798_63C56280L, 0x7B2B9908_3CD1429CL,
					0xDBE89E25_BFBD7E3CL, 0xAD3173B2_E3C5A0B1L, 0x74DA6D53_91E6A06EL, 0x465F037A_40062548L,
					0x39A56BF7_6DA84B1CL, 0x94E0AE20_8576156FL, 0xE5C140A4_BA4FFC9EL, 0x38C3B07B_88845FC6L,
					0xF7DDDA93_381FE0CAL, 0x6084C4CD_2D336E54L, 0x51C464CC_B6EC65E7L, 0xD16E548A_273E8262L,
					0x84AF2559_B6264274L, 0x215960FF_F47BDD63L, 0xD3AFF064_D6137AF7L, 0x69661C9D_4FEE4738L,
					0x2603C88E_AA098058L, 0x1D077584_61B777E4L, 0x356DDA58_35198B51L, 0xFEEA308D_70F75450L,
					0xB71675C0_8C7D8302L, 0xFD7539DD_1FF2A11CL, 0xB4258AA7_0D234436L, 0xAA42B6A0_615F3F91L,
					0x5D55CC3B_966B2716L, 0xB36E4D1A_06CE5E5DL, 0x2EA3BEE5_A1270E87L, 0x51DA45B6_0B997B0FL,
					0xFDB0F996_2FEE4F03L, 0xBEE780BA_0A845B1DL, 0x92714217_83AE6601L, 0xA61EA2E3_42E4F2E8L,
					0xBC935A40_9EAD19F2L, 0x21BD1B74_E2964DD1L, 0x9FC845F6_0EFC0933L, 0x8B60B6B2_56D8CAC8L,
					0x89CCA306_CC370A0BL, 0x18C8B886_E95DA0AFL, 0x5235FEF4_393020D2L, 0xB7F30569_04759042L });

	public static final byte[] u = toByteArray(new long[] { 
					0x03AE5F3C_3FA9EFF1L, 0xA50D7DBB_8D2F60A1L, 0xEA66EA71_2D50AE97L, 0x6EE34641_A1CD0E51L,
					0xC4683DA3_83E8595DL, 0x6CB56A15_D5FBC754L, 0x3E07FBDD_D316217EL, 0x01A391A1_8EF06DFFL});

	public static final byte[] premasterSecret = toByteArray(
			new long[] { 
					0xB0DC82BA_BCF30674L, 0xAE450C02_87745E79L, 0x90A3381F_63B387AAL, 0xF271A10D_233861E3L,
					0x59B48220_F7C4693CL, 0x9AE12B0A_6F67809FL, 0x0876E2D0_13800D6CL, 0x41BB59B6_D5979B5CL,
					0x00A172B4_A2A5903AL, 0x0BDCAF8A_709585EBL, 0x2AFAFA8F_3499B200L, 0x210DCC1F_10EB3394L,
					0x3CD67FC8_8A2F39A4L, 0xBE5BEC4E_C0A3212DL, 0xC346D7E4_74B29EDEL, 0x8A469FFE_CA686E5AL });

	
	public static final byte[] K = toByteArray(new long[] { 
			0x5CBC219D_B052138EL, 0xE1148C71_CD449896L, 0x3D682549_CE91CA24L, 0xF098468F_06015BEBL,
			0x6AF245C2_093F98C3L, 0x651BCA83_AB8CAB2BL, 0x580BBF02_184FEFDFL, 0x26142F73_DF95AC50L
	});
	
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

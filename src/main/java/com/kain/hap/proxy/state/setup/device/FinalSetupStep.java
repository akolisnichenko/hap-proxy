package com.kain.hap.proxy.state.setup.device;

import com.google.crypto.tink.subtle.Ed25519Verify;
import com.kain.hap.proxy.crypto.AEADResult;
import com.kain.hap.proxy.crypto.ChaCha20;
import com.kain.hap.proxy.crypto.HKDF;
import com.kain.hap.proxy.crypto.Hash;
import com.kain.hap.proxy.service.DeviceSessionService;
import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.srp.DeviceSession;
import com.kain.hap.proxy.srp.Session;
import com.kain.hap.proxy.state.BehaviourStep;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.serialize.TlvMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinalSetupStep extends BaseDeviceStep {

	public FinalSetupStep(DeviceSessionService sessionService) {
		super(sessionService);
	}

	@Override
	public Packet handle(StateContext context) {
		Session session = getSessionService().getSession();
		log.info("final setup pair step on device");

		HKDF hkdf = new HKDF(Hash::hmacSha512);
		hkdf.extract("Pair-Setup-Accessory-Sign-Salt".getBytes(), session.getSharedSessionKey());
		byte[] inKey = hkdf.expand("Pair-Setup-Accessory-Sign-Info".getBytes(), 32);
		Packet packet = context.getIncome();

		byte[] data = packet.getEncrypted().getArray();
		try {
			byte[] res = ChaCha20.decode(inKey, "PS-Msg06".getBytes(), new AEADResult(data), new byte[0]);
			Packet dataPacket = TlvMapper.readPacket(res);
			byte[] accessoryLtpk = dataPacket.getKey().getKey();
			byte[] id = dataPacket.getId().getId();
			byte[] signature = dataPacket.getSignature().getValue();
			
			Ed25519Verify verify = new Ed25519Verify(accessoryLtpk);
			verify.verify(signature, id);
			//FIXME: register accessory data
			log.error("not implemented store accessory LTPK");
			//sessionService.registerDevicePairing(null, accessoryLtpk);
		} catch (Exception e) {
			log.error("Invalid exchange step", e);
		}
		return null;
	}
}

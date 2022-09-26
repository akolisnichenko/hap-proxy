package com.kain.hap.proxy.state.setup;

import com.kain.hap.proxy.service.DeviceSession;
import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.DeviceProofPacket;
import com.kain.hap.proxy.tlv.packet.SaltPacket;
import com.kain.hap.proxy.tlv.type.Proof;
import com.kain.hap.proxy.tlv.type.SrpPublicKey;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeviceProofStep implements BehaviourState {
	private final SessionRegistrationService sessionService;

	@Override
	public BasePacket handle(StateContext context) {
		DeviceSession session = sessionService.getDeviceSession(context.getDeviceId());
		SaltPacket income = (SaltPacket)context.getIncome();
		byte[] proof = session.respond(income.getPublicKey(), income.getSalt());
		//TODO: return correct packet as response
		return new DeviceProofPacket(State.M3, new Proof(proof), new SrpPublicKey(session.getPublicKey()));
	}

}

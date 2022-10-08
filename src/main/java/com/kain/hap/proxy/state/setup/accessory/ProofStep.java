package com.kain.hap.proxy.state.setup.accessory;

import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.srp.AccessorySession;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.DeviceProofPacket;
import com.kain.hap.proxy.tlv.packet.ProofPacket;
import com.kain.hap.proxy.tlv.type.Proof;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProofStep implements BehaviourState {
	private final SessionRegistrationService sessionService;

	@Override
	public BasePacket handle(StateContext context) {
		AccessorySession session = sessionService.getSession(context.getDeviceId());
		DeviceProofPacket income = (DeviceProofPacket)context.getIncome();
		byte[] proof = session.respond(income.getPublicKey(), income.getProof());
		return new ProofPacket(State.M4, new Proof(proof));
	}
}

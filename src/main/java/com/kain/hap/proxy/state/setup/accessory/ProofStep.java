package com.kain.hap.proxy.state.setup.accessory;

import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.srp.AccessorySession;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.type.Proof;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProofStep implements BehaviourState {
	private final SessionRegistrationService sessionService;

	@Override
	public Packet handle(StateContext context) {
		AccessorySession session = sessionService.getSession(context.getDeviceId());
		byte[] proof = session.respond(context.getIncome().getKey(), context.getIncome().getProof());
		return Packet.builder()
				.state(State.M4)
				.proof(new Proof(proof))
				.build();
	}
}

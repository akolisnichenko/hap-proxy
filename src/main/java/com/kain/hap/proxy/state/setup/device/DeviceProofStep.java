package com.kain.hap.proxy.state.setup.device;

import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.srp.DeviceSession;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.type.Proof;
import com.kain.hap.proxy.tlv.type.PublicKey;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeviceProofStep implements BehaviourState {
	private final SessionRegistrationService sessionService;

	@Override
	public Packet handle(StateContext context) {
		DeviceSession session = sessionService.registerDevice(context.getDeviceId());
		byte[] proof = session.respond(context.getIncome().getKey(), context.getIncome().getSalt());
		return Packet.builder()
				.state(State.M3).proof(new Proof(proof))
				.key(new PublicKey(session.getPublicKey()))
				.build();
	}
}

package com.kain.hap.proxy.state.setup.accessory;

import com.kain.hap.proxy.service.AccessorySessionService;
import com.kain.hap.proxy.srp.Session;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.type.Proof;


public class ProofStep extends BaseAccessoryStep {
	
	public ProofStep(AccessorySessionService sessionService) {
		super(sessionService);
	}

	@Override
	public Packet handle(StateContext context) {
		Session session = getSessionService().getSession();
		byte[] proof = session.respond(context.getIncome().getKey(), context.getIncome().getProof().getProof());
		return Packet.builder()
				.state(State.M4)
				.proof(new Proof(proof))
				.build();
	}
}

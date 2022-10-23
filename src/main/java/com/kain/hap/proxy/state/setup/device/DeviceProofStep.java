package com.kain.hap.proxy.state.setup.device;


import com.kain.hap.proxy.service.DeviceSessionService;
import com.kain.hap.proxy.srp.Session;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.type.Proof;


public class DeviceProofStep extends BaseDeviceStep {

	public DeviceProofStep(DeviceSessionService sessionService) {
		super(sessionService);
	}

	@Override
	public Packet handle(StateContext context) {
		Session session = getSessionService().getSession();
		byte[] proof = session.respond(context.getIncome().getKey(), context.getIncome().getSalt().getSalt());
		return Packet.builder()
				.state(State.M3).proof(new Proof(proof))
				.key(session.getPublicKey())
				.build();
	}
}

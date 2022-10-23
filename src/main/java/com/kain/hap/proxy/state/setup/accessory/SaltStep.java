package com.kain.hap.proxy.state.setup.accessory;

import com.kain.hap.proxy.service.AccessorySessionService;
import com.kain.hap.proxy.srp.Session;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.type.Salt;



public class SaltStep extends BaseAccessoryStep {

	public SaltStep(AccessorySessionService sessionService) {
		super(sessionService);
	}

	@Override
	public Packet handle(StateContext context) {
		Session session = getSessionService().registerSession();
		return Packet.builder()
				.state(State.M2)
				.salt(new Salt(session.getSalt()))
				.key(session.getPublicKey())
				.build();
	}

}

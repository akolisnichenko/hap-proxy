package com.kain.hap.proxy.state;

import com.kain.hap.proxy.tlv.packet.Packet;

public interface BehaviourStep {
	Packet handle(StateContext context);

}

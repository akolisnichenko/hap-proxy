package com.kain.hap.proxy.state;

import com.kain.hap.proxy.tlv.packet.Packet;

public interface BehaviourState {
	Packet handle(StateContext context);

}

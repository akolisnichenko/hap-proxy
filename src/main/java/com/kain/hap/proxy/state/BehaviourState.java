package com.kain.hap.proxy.state;

import com.kain.hap.proxy.tlv.packet.BasePacket;

public interface BehaviourState {
	
	BasePacket handle(StateContext context);

}

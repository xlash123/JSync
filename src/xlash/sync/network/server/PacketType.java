package xlash.sync.network.server;

public enum PacketType {
	
	NULL((byte) 0), MOUSE((byte) 1), SCREENS((byte) 2), KEYDOWN((byte) 3), KEYUP((byte) 4);
	
	public byte id;
	
	PacketType(byte id) {
		this.id = id;
	}
	
	public static PacketType getPacketType(byte id) {
		for(PacketType pt : PacketType.values()) {
			if(pt.id == id) return pt;
		}
		
		return NULL;
	}

}

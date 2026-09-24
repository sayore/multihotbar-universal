# 1.20 compatibility adapter

Minecraft 1.20.1 predates the 1.20.5 Data Component migration used by the canonical implementation.
The runtime contract is unchanged, but storage must be NBT-backed and packet registration must use the loader APIs for this MC line.
 therefore treats this target as a compile gate, not as pre-verified binary output.

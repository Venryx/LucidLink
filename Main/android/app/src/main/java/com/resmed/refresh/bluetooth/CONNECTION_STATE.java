package com.resmed.refresh.bluetooth;

public enum CONNECTION_STATE
{
	BLUETOOTH_OFF("BLUETOOTH_OFF", 3),
	BLUETOOTH_ON("BLUETOOTH_ON", 2),
	NIGHT_TRACK_OFF("NIGHT_TRACK_OFF", 1),
	NIGHT_TRACK_ON("NIGHT_TRACK_ON", 11),
	REAL_STREAM_OFF("REAL_STREAM_OFF", 0),
	REAL_STREAM_ON("REAL_STREAM_ON", 10),
	SESSION_OPENED("SESSION_OPENED", 9),
	SESSION_OPENING("SESSION_OPENING", 8),
	SOCKET_BROKEN("SOCKET_BROKEN", 4),
	SOCKET_CONNECTED("SOCKET_CONNECTED", 7),
	SOCKET_NOT_CONNECTED("SOCKET_NOT_CONNECTED", 6),
	SOCKET_RECONNECTING("SOCKET_RECONNECTING", 5);

	static int[] $SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE;
	static /* synthetic */ int[] $SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE() {
		final int[] $switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE = CONNECTION_STATE.$SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE;
		if ($switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE != null) {
			return $switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE;
		}
		final int[] $switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2 = new int[values().length];
		while (true) {
			try {
				$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.BLUETOOTH_OFF.ordinal()] = 4;
				try {
					$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.BLUETOOTH_ON.ordinal()] = 3;
					try {
						$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal()] = 2;
						try {
							$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()] = 12;
							try {
								$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.REAL_STREAM_OFF.ordinal()] = 1;
								try {
									$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.REAL_STREAM_ON.ordinal()] = 11;
									try {
										$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.SESSION_OPENED.ordinal()] = 10;
										try {
											$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.SESSION_OPENING.ordinal()] = 9;
											try {
												$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.SOCKET_BROKEN.ordinal()] = 5;
												try {
													$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.SOCKET_CONNECTED.ordinal()] = 8;
													try {
														$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.SOCKET_NOT_CONNECTED.ordinal()] = 7;
														try {
															$switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2[CONNECTION_STATE.SOCKET_RECONNECTING.ordinal()] = 6;
															return CONNECTION_STATE.$SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE = $switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2;
															//return $switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE2;
														}
														catch (NoSuchFieldError noSuchFieldError) {}
													}
													catch (NoSuchFieldError noSuchFieldError2) {}
												}
												catch (NoSuchFieldError noSuchFieldError3) {}
											}
											catch (NoSuchFieldError noSuchFieldError4) {}
										}
										catch (NoSuchFieldError noSuchFieldError5) {}
									}
									catch (NoSuchFieldError noSuchFieldError6) {}
								}
								catch (NoSuchFieldError noSuchFieldError7) {}
							}
							catch (NoSuchFieldError noSuchFieldError8) {}
						}
						catch (NoSuchFieldError noSuchFieldError9) {}
					}
					catch (NoSuchFieldError noSuchFieldError10) {}
				}
				catch (NoSuchFieldError noSuchFieldError11) {}
			}
			catch (NoSuchFieldError noSuchFieldError12) {
				continue;
			}
			break;
		}
		return $switch_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE;
	}

	private CONNECTION_STATE(final String s, final int n) {
	}

	public static String toString(final CONNECTION_STATE connection_STATE) {
		switch ($SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE()[connection_STATE.ordinal()]) {
			default: {
				return "UNKNOW";
			}
			case 9: {
				return "SESSION_OPENING";
			}
			case 10: {
				return "SESSION_OPENED";
			}
			case 3: {
				return "BLUETOOTH_ON";
			}
			case 4: {
				return "BLUETOOTH_OFF";
			}
			case 11: {
				return "REAL_STREAM_ON";
			}
			case 1: {
				return "REAL_STREAM_OFF";
			}
			case 12: {
				return "NIGHT_TRACK_ON";
			}
			case 2: {
				return "NIGHT_TRACK_OFF";
			}
			case 6: {
				return "SOCKET_RECONNECTING";
			}
			case 8: {
				return "SOCKET_CONNECTED";
			}
			case 7: {
				return "SOCKET_NOT_CONNECTED";
			}
			case 5: {
				return "SOCKET_BROKEN";
			}
		}
	}
}

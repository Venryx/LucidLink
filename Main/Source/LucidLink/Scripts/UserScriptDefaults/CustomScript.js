export default `
// Notes
// ========== 

/*
* To get the key-code of key (so you can add a keypress-listener), click the "More" tab and
		press the desired key; you'll see the key-code in the logs
*/

// Examples
// ==========

// when key "1" is pressed, start the air-raid-siren sound
AddKeyDownListener(8, function() {
	GetAudioFile("air raid siren").Play();
});
// when key "2" is pressed, stop the air-raid-siren sound
AddKeyDownListener(9, function() {
	GetAudioFile("air raid siren").Stop();
});

GetAudioFile("air raid siren"); // preload
`.trim();
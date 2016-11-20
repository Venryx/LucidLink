export default `
// the below are some examples of actions and behaviors you can set up through scripting
// feel free to remove or modify

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
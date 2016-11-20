export default `
// this script should only be modified minimally (eg commenting stuff out, tweaking values)
// most custom code should be placed in the "custom script" file

// when key "1" is pressed, start the air-raid-siren sound
AddKeyDownListener(8, function() { GetAudioFile("air raid siren").Play(); });
// when key "2" is pressed, stop the air-raid-siren sound
AddKeyDownListener(9, function() { GetAudioFile("air raid siren").Stop(); });

GetAudioFile("air raid siren"); // preload
`.trim();
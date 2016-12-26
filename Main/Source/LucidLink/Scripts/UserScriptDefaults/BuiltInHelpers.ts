export default `
// the below is a list of the most important functions you can call from your scripts

// storing/displaying messages
// ==========

alert("This text is displayed in a popup-dialog.")
Log("This text is added to the program log, visible in the More>Logs panel, and also saved to the session's log file.")
Toast("This text is displayed at the bottom of the screen as a 'toast' message. (closes automatically)")
Notify("This text is displayed at the bottom of the screen as a persistent 'snackbar' message. (closes by pressing Dismiss)")
AddEvent("special event", "First piece of extra data (argument).", "Second piece/argument.")

// general
// ==========

WaitXThenRun(1000, function() {
	// this code runs after a delay of 1 second (1000 milliseconds)
})
var randomNumber = GetRandomNumber({min: 1, max: 10, mustBeInteger: true})

// input
// ==========

// for a list of keycodes, see here: https://developer.android.com/ndk/reference/keycodes_8h.html
// (you can also just press the key you want, then see its keycode in the Logs panel)
var keyCodeForTheLetterA = 29
AddKeyDownListener(keyCodeForTheLetterA, function() {
	// this code runs whenever the user presses the letter A
})
AddKeyUpListener(keyCodeForTheLetterA, function() {
	// this code runs whenever the user releases the letter A
})

// muse
// ==========

WhenChangeMuseConnectStatus(function(status) {
	Log("New status: " + status)
})
WhenMusePacketReceived(function(packet) {
	Log("Received packet: " + ToJSON(packet))
})

// pattern matching
// ==========

AddListener_OnUpdatePatternMatchProbabilities(function(patternMatchProbabilities) {
	// this code runs whenever the EEG monitor updates the probabilities for pattern-matches
	var probabilityOfGlanceUpMatch = patternMatchProbabilities["glance-up"]
	Log("We're " + (probabilityOfGlanceUpMatch * 100) + "% sure a glance-up action just occurred.")
})

// audio playback
// ==========

GetAudioFile("air raid siren").Play()
GetAudioFile("air raid siren").SetVolume(.5) // range: 0 to 1
GetAudioFile("air raid siren").FadeVolume({to: 1, over: 10}) // time in seconds
GetAudioFile("air raid siren").SetCurrentTime(5) // set playback position, in seconds
GetAudioFile("air raid siren").PlayCount = -1 // set number of times audio should play; -1 means "loop forever"
GetAudioFile("air raid siren").Pause()
GetAudioFile("air raid siren").Stop()

// text-to-speech
// ==========

Speak({
	text: "This is the text that will be spoken.",
	pitch: 1.5, // the pitch of the voice
	forceStop: false, // if true, this will stop any prior speech commands that are still in progress
	language: "en", country: "US"
})
`.trim();
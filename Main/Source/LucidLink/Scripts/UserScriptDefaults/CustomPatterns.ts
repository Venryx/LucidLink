export default `
// glance pattern
// ==========

var peakHeight = 200
var gapMin = 70
var gapMax = 700

var segments = []
for (var i = 0; i < 100; i++) {
	if (i > 0) 
		segments.push(new Gap({min: gapMin, max: gapMax}))
	segments.push(new Matcher(function(packet) { return packet.GetChannelOffset("fl") >= peakHeight }))
	segments.push(new Gap({min: gapMin, max: gapMax}))
	segments.push(new Matcher(function(packet) { return packet.GetChannelOffset("fr") >= peakHeight }))
}

var lastGlanceAttempt
AddPattern({
	minStartInterval: 300,
	maxOverlappingAttempts: 1,
	segments: segments,
	onPartialMatch: function(matchAttempt) {
		lastGlanceAttempt = matchAttempt
		if (matchAttempt.SegmentsMatched % 2 == 0) return // ignore gap-segment matches

		var glances = 1 + parseInt(matchAttempt.SegmentsMatched / 2)
		if (glances >= 8) {
			//Speak({text: glances})

			if (!waker.IsPlaying())
				waker.Play()
			var percentFrom8To20 = (glances - 8) / 12
			waker.SetVolume(percentFrom8To20)
		}
	},
})

// blink pattern
// ==========

var peakHeight = 200
var gapMin = 15
var gapMax = 70

var segments = []
for (var i = 0; i < 100; i++) {
	if (i > 0) 
		segments.push(new Gap({min: gapMin, max: gapMax}))
	segments.push(new Matcher(function(packet) { return packet.GetChannelOffset("fl") >= peakHeight }))
	segments.push(new Gap({min: gapMin, max: gapMax}))
	segments.push(new Matcher(function(packet) { return packet.GetChannelOffset("fr") >= peakHeight }))
}

AddPattern({
	minStartInterval: 300,
	maxOverlappingAttempts: 1,
	segments: segments,
	onPartialMatch: function(matchAttempt) {
		if (matchAttempt.SegmentsMatched % 2 == 0) return // ignore gap-segment matches

		var blinks = 1 + parseInt(matchAttempt.SegmentsMatched / 2)
		if (blinks >= 7) {
			waker.Stop()
			//matchAttempt.Cancel()
			if (lastGlanceAttempt)
				lastGlanceAttempt.Cancel()
		}
	},
})
`.trim();
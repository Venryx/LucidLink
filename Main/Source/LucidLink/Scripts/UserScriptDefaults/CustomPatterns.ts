export default `
// for night
var lrPeakHeight = 300
var gapMin = 30
var gapMax = 500

// for demo
/*/
lrPeakHeight = 200
gapMax = 500
/**/

var segments = []
for (var i = 0; i < 10; i++) {
	if (i > 0) 
		segments.push(new Gap({min: gapMin, max: gapMax}))
	segments.push(new Matcher(function(packet) { return packet.GetChannelValDif("fl") >= lrPeakHeight }))
	//segments.push(new Matcher(function(packet) { return true; }))
	segments.push(new Gap({min: gapMin, max: gapMax}))
	segments.push(new Matcher(function(packet) { return packet.GetChannelValDif("fr") >= lrPeakHeight }))
}

AddPattern({
	minStartInterval: 300,
	maxOverlappingAttempts: 1,
	segments: segments,
	onPartialMatch: function(matchAttempt) {
		if (matchAttempt.segmentsMatched % 2 == 0) return; // ignore gap-segment matches
		var glances = 1 + parseInt(matchAttempt.segmentsMatched / 2)
		if (glances <= 1) return

		//BufferAction(1000, function() {
			var text = glances.toString()
			//Toast("Matched: " + text)
			//Notify("Matched: " + text)
			Speak({text: text})
		//})
	},
});
`.trim();
export default `
// startup
// ==========

var waterfall = GetAudioFile("waterfall")
waterfall.PlayCount = -1 // loop
waterfall.Stop()

//AddEvent("SS") // starting scripts
`.trim();
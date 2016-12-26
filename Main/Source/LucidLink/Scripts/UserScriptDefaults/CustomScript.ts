export default `
// startup
// ==========

var waker = GetAudioFile("game")
waker.PlayCount = -1 // loop
waker.Stop()

AddEvent("SS") // starting scripts

var lastPacket = {}
WhenMusePacketReceived(function(packet) {
    lastPacket = packet
});

var lastProcessedPacket = null;
EveryXSecondsDo(1, function() {
    if (lastPacket == null || lastPacket == lastProcessedPacket) return
    lastProcessedPacket = lastPacket

    var distInMeters = parseInt(V.Lerp(0, 10, lastPacket.viewDistance))
    var text = (lastPacket.viewDirection * 100).toFixed(0) //+ " " + distInMeters
    //Speak({text: text, pitch: 1})
})
`.trim();
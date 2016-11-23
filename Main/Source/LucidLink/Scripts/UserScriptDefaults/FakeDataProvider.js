export default `
var lastColumn = [0, 0, 0, 0, 0, 0];
var random = new Random();
var timer = new TimerMS(100, function() {
	var column = [];
	for (var channel = 0; channel < 6; channel++)
		column[channel] = lastColumn[channel] + ((random.NextDouble() - .5) * 3);
	MuseBridge.OnReceiveMuseDataPacket("eeg", column);
	lastColumn = column;
});
timer.Start();
`.trim();
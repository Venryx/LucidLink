export default `
var lastColumn = [0, 0, 0, 0, 0, 0];
var random = new Random();
var timer = new TimerMS(100, ()=> {
	var column = [];
	for (var i = 0; i < 6; i++)
		column[i] = lastColumn[i] + (random.NextDouble() * 3);
	MuseBridge.OnReceiveMuseDataPacket("eeg", column);
	lastColumn = column;
});
timer.Start();
`.trim();
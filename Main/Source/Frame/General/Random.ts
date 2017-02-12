export class Random {
	static canvasContext;
	static canvas;

	constructor(seed = new Date().getTime()) {
		if (seed == 0)
			throw new Error("Cannot use 0 as seed. (prng algorithm isn't very good, and doesn't work with seeds that are multiples of PI)");
		this.seed = seed;
	}
	seed = -1;
	NextInt() {
		var randomDouble = Math.sin(this.seed) * 10000;
		var randomDouble_onlyIntegerPart = Math.floor(this.seed);
		this.seed = randomDouble;
		return randomDouble_onlyIntegerPart;
	}
	NextDouble() {
		var randomDouble = Math.sin(this.seed) * 10000;
		var randomDouble_onlyFractionalPart = this.seed - Math.floor(this.seed);
		this.seed = randomDouble;
		return randomDouble_onlyFractionalPart;
	}
	/*NextColor() { return new VColor(s.NextDouble(), s.NextDouble(), s.NextDouble()); };
	NextColor_ImageStr() {
		var color = this.NextColor();
		Random.canvasContext.fillStyle = color.ToHexStr();
		Random.canvasContext.fillRect(0, 0, Random.canvas[0].width, Random.canvas[0].height);
		var imageStr = Random.canvas[0].toDataURL();
		return imageStr.substr(imageStr.indexOf(",") + 1);
	}*/
}
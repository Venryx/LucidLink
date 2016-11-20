export default `
// key-codes can be found here: https://developer.android.com/ndk/reference/keycodes_8h.html
function AddKeyDownListener(keyCode, func) {
	LL.scripts.scriptRunner.keyDownListeners.push({keyCode, func});
}
function AddKeyUpListener(keyCode, func) {
	LL.scripts.scriptRunner.keyDownListeners.push({keyCode, func});
}
`.trim();
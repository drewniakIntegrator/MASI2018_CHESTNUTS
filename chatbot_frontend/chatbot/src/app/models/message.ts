export class Message {

    date: string;
    text: string;
    isHuman: boolean;

    constructor(date: string, text: string, isHuman: boolean ) {
        this.date = new Date().toLocaleTimeString();
        this.text = text;
        this.isHuman = isHuman;
    }
}

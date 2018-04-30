export class Message {

    date: string;
    text: string;
    isHuman: boolean;
    hints: string[];

    constructor(date: string, text: string, isHuman: boolean, hints?: string[]) {
        this.date = new Date().toLocaleTimeString();
        this.text = text;
        this.isHuman = isHuman;
        this.hints = hints || [];
    }
}

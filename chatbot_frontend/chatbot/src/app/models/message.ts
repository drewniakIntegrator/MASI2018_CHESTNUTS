import { Item } from "./item";

export class Message {

    date: string;
    text: string;
    isHuman: boolean;
    hints: string[];
    items: Item[];

    constructor(date: string, text: string, isHuman: boolean, hints?: string[], items?: Item[]) {
        this.date = new Date().toLocaleTimeString();
        this.text = text;
        this.isHuman = isHuman;
        this.hints = hints || [];
        this.items = items || [];
    }
}

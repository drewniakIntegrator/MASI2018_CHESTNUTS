export class Message {

    date: string;
    text: string;
    isHuman: boolean;
    nameUser: string;

    constructor(date: string, text: string, isHuman: boolean, nameUser: string) {
        this.date = new Date().toLocaleTimeString();
        this.text = text;
        this.isHuman = isHuman;
        this.nameUser = nameUser;
    }
}

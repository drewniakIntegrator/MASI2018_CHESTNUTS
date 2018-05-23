export class RequestObject {
    conversationId: string;
    message: string;

    constructor(msg: string, id: string) {
        this.message = msg;
        this.conversationId = id;
    }
}

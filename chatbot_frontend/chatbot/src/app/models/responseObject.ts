import { Message } from "./message";

export class ResponseObject {
    conversationId: string;
    message: string;
    url: string;

    public parseForMessage(): Message {
        console.log('a');
        return null;
    }
}

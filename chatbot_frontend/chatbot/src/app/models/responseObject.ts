import { Item } from "./item";

export class ResponseObject {
    conversationId: string;
    message: string;
    url: string;
    hints: string[] = [];
    items: Item[] = [];
    isFinal: boolean;
}

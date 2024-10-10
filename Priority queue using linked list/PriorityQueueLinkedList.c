#include<stdio.h>
#include<stdlib.h>

typedef struct node {
    int data;
    int priority;
    struct node* next;
} Node;

Node* newNode(int d, int p) {
    Node* temp = (Node*)malloc(sizeof(Node));
    temp->data = d;
    temp->priority = p;
    temp->next = NULL;

    return temp;
}

int peek(Node** head) {
    return (*head)->data;
}

void pop(Node** head) {
    Node* temp = *head;
    (*head) = (*head)->next;
    free(temp);


}
void highest_lowest(Node** head) {
    if (*head == NULL) {
        printf("Priority queue is empty.\n");
        return;
    }

    Node* highest = *head;
    Node* lowest = *head;
    Node* current = *head;

    while (current != NULL) {
        if (current->priority > highest->priority) {
            highest = current;
        }
        if (current->priority < lowest->priority) {
            lowest = current;
        }
        current = current->next;
    }

    printf("Node with highest priority: %d\n", highest->data);
    printf("Node with lowest priority: %d\n", lowest->data);
}

void push(Node** head, int d, int p)
{
    Node* start = (*head);

    //create the newnode
    Node* temp = newNode(d, p);

    //special cases: when the head of the list has less priority than the new node
    //Insert new node before the head node and change the head node
    if ((*head)->priority > p) {
        //insert the new node before head
        temp->next = *head;
        (*head) = temp;
    }

    else {
        //traverse the list and find the position where the new node has higher priority
        while (start->next != NULL && start->next->priority < p) {
            start = start->next;
        }
        //insertion can be either at the end
        //or the position whether the priority suits
        temp->next = start->next;
        start->next = temp;
    }

}

int isEmpty(Node** head) {
    return (*head == NULL);

}

int main() {

    // 7->4->5->6
    // pq is the first node, hence the head of the queue
    Node* pq = newNode(4, 1);
    push(&pq, 5, 2);
    push(&pq, 6, 3);
    push(&pq, 7, 0);


    while (!isEmpty(&pq)) {
        printf("%d ", peek(&pq));
        pop(&pq);
    }
}
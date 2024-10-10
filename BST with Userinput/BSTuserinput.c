#include <stdio.h>
#include <stdlib.h>
#define _CRT_SECURE_NO_WARNINGS 
//binary search tree with post order traversal
//this is for defining the structure of the node
typedef struct treenode {
    int data;
    struct treenode* left;
    struct treenode* right;
} node;

//function to insert node into the tree
void insertNode(node** root, int data) {
    node* temp = (node*)malloc(sizeof(node));
    //check for if temp is null. I was getting C6011 warning that node was derefernced
    if (temp == NULL) {
        printf("Memory allocation failed");
        return;
    }
    temp->data = data;
    temp->left = NULL;
    temp->right = NULL;

    if (*root == NULL) {
        *root = temp;
        return;
    };

    if (data < (*root)->data) {
        insertNode(&((*root)->left), data);
    }
    else {
        insertNode(&((*root)->right), data);
    }
}

//function to display the tree in post order traversal
void display(node* root) {
    if (root != NULL) {
        display(root->left);
        display(root->right);
        printf("%d \n", root->data);
    }
}

//main function
int main() {
    node* root = NULL;
    int n, data;
    printf("enter number of nodes:");
    scanf_s("%d", &n);

    //for loop to insert nodes of the tree form user
    for (int i = 0; i < n; i++) {
        printf("enter node data: ");
        scanf_s("%d", &data);
        insertNode(&root, data);
    }

    printf("BST in post-order traversal:\n");
    display(root);
    return 0;
}

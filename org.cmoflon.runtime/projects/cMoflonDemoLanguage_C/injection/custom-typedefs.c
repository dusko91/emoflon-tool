typedef struct  {
	EDouble k;
	NODE_T* node;
}KTCALGORITHM_T;

typedef struct {
	EDouble k;
	EDouble stretchFactor;
	NODE_T* node;
}LSTARKTCALGORITHM_T;

// Forward declaration
struct TREE_T;

typedef struct {
	NODE_T* node;
	struct TREE_T* tree;
}LMSTALGORITHM_T;

typedef struct TREE_T{
	LMSTALGORITHM_T* algo;
	list_t entries;
	struct memb* mem;
}TREE_T;

typedef struct {
	struct TREEENTRY_T* next;
	NODE_T* node;
	LINK_T* selectedLink;
	TREE_T* algorithm;
	bool isInTree;
}TREEENTRY_T;

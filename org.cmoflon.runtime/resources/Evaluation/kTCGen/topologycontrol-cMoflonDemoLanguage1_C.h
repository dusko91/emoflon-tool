#ifndef __TOPOLOGYCONTROL__CMOFLONDEMOLANGUAGE1_C_H_
#define __TOPOLOGYCONTROL__CMOFLONDEMOLANGUAGE1_C_H_

#include <stdlib.h>
#include <stdio.h>
#include <float.h>
#include "contiki.h"
#include "contiki-lib.h"
#include "../../app-conf.h"
#include "../../lib/boot.h"
#include "../../lib/components.h"
#include "../../lib/utilities.h"
#include "../../lib/neighbors.h"
#include "../../lib/networkaddr.h"
#include "dev/watchdog.h"

#ifndef COMPONENT_TOPOLOGYCONTROL_CMOFLONDEMOLANGUAGE1_C_K
#define COMPONENT_TOPOLOGYCONTROL_CMOFLONDEMOLANGUAGE1_C_K 1.2
#endif

#ifndef COMPONENT_TOPOLOGYCONTROL_CMOFLONDEMOLANGUAGE1_C_UPDATEINTERVAL
#define COMPONENT_TOPOLOGYCONTROL_CMOFLONDEMOLANGUAGE1_C_UPDATEINTERVAL 300
#endif

#ifndef MAX_MATCH_COUNT
#define MAX_MATCH_COUNT 30
#endif
typedef struct match{
	struct match_t* next;
	void** match;
}match_t;

typedef networkaddr_t NODE_T;


typedef neighbor_t LINK_T;


typedef bool EBoolean;

typedef double EDouble;

typedef int EInt;

// --- Begin of default cMoflon type definitions
typedef struct {
	NODE_T* node;
}TOPOLOGYCONTROLALGORITHM_T;

// --- End of default cMoflon type definitions

// --- Begin of user-defined type definitions (from path 'injection/custom-typedefs.c')
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
	LINK_T* parent;
	TREE_T* tree;
	bool isInTree;
}TREEENTRY_T;
// --- End of user-defined type definitions

//Begin of non SDM implemented methods
void ktcAlgorithm_run(KTCALGORITHM_T* this);
//End of non SDM implemented methods

//Begin of declarations for hopcount
EInt node_getHopcount(NODE_T* _this);
void node_setHopcount(NODE_T* _this, EInt value);
//End of declarations for hopcount

//Begin of declarations for incomingLinks
list_t node_getIncomingLinks(NODE_T* _this);
void node_addIncomingLinks(NODE_T* _this, LINK_T* value);
void node_removeIncomingLinks(NODE_T* _this, LINK_T* item);
bool node_containsIncomingLinks(NODE_T* _this, LINK_T* value);
bool node_isIncomingLinks(void* candidate, void* _this);
//End of declarations for incomingLinks

//Begin of declarations for outgoingLinks
list_t node_getOutgoingLinks(NODE_T* _this);
void node_addOutgoingLinks(NODE_T* _this, LINK_T* value);
void node_removeOutgoingLinks(NODE_T* _this, LINK_T* item);
bool node_containsOutgoingLinks(NODE_T* _this, LINK_T* value);
bool node_isOutgoingLinks(void* candidate, void* _this);
//End of declarations for outgoingLinks

//Begin of declarations for neighborhood
list_t node_getNeighborhood(NODE_T* _this);
void node_addNeighborhood(NODE_T* _this, LINK_T* value);
void node_removeNeighborhood(NODE_T* _this, LINK_T* item);
bool node_containsNeighborhood(NODE_T* _this, LINK_T* value);
bool node_isNeighborhood(void* candidate, void* _this);
//End of declarations for neighborhood

//Begin of declarations for weight
EDouble link_getWeight(LINK_T* _this);
void link_setWeight(LINK_T* _this, EDouble value);
//End of declarations for weight

//Begin of declarations for marked
LinkState link_getMarked(LINK_T* _this);
void link_setMarked(LINK_T* _this, LinkState value);
//End of declarations for marked

//Begin of declarations for target
NODE_T* link_getTarget(LINK_T* _this);
void link_setTarget(LINK_T* _this, NODE_T* value);
//End of declarations for target

//Begin of declarations for source
NODE_T* link_getSource(LINK_T* _this);
void link_setSource(LINK_T* _this, NODE_T* value);
//End of declarations for source

//Begin of declarations for k
EDouble ktcAlgorithm_getK(KTCALGORITHM_T* _this);
void ktcAlgorithm_setK(KTCALGORITHM_T* _this, EDouble value);
//End of declarations for k

//Begin of declarations for node
NODE_T* ktcAlgorithm_getNode(KTCALGORITHM_T* _this);
void ktcAlgorithm_setNode(KTCALGORITHM_T* _this, NODE_T* value);
//End of declarations for node

//Begin of compare declarations
int eBoolean_compare(EBoolean _this, EBoolean other);
int eDouble_compare(EDouble _this, EDouble other);
int eInt_compare(EInt _this, EInt other);
int node_compare(NODE_T* _this, NODE_T* other);
int link_compare(LINK_T* _this, LINK_T* other);
int topologyControlAlgorithm_compare(TOPOLOGYCONTROLALGORITHM_T* _this, TOPOLOGYCONTROLALGORITHM_T* other);
int ktcAlgorithm_compare(KTCALGORITHM_T* _this, KTCALGORITHM_T* other);
//End of compare declarations

//Begin of equals declarations
bool eBoolean_equals(EBoolean _this, EBoolean other);
bool eDouble_equals(EDouble _this, EDouble other);
bool eInt_equals(EInt _this, EInt other);
bool node_equals(NODE_T* _this, NODE_T* other);
bool link_equals(LINK_T* _this, LINK_T* other);
bool topologyControlAlgorithm_equals(TOPOLOGYCONTROLALGORITHM_T* _this, TOPOLOGYCONTROLALGORITHM_T* other);
bool ktcAlgorithm_equals(KTCALGORITHM_T* _this, KTCALGORITHM_T* other);
//End of equals declarations

#endif /* __TOPOLOGYCONTROL__CMOFLONDEMOLANGUAGE1_C_H_ */
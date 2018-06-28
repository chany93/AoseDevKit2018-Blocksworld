;; domain file: delivery-domain.pddl

(define (domain delivery-domain)
	(:requirements :strips)
	(:predicates    (restaurant ?r) (food ?f) (delivery_boy ?b) (client ?c) (location ?l)
					(waiting ?r)
					(has_food ?r ?f)
					(ready_at_food ?r ?f)
					(free ?b)
					(with_food ?b ?f)
					(next_to ?dep ?dest)
					(at ?l ?b)
					(served ?c ?f)
					(me ?r)
					(not-me ?r)
					(cost-rest ?r ?n)
					(rating-rest ?r ?n)
	)

	(:action prepare_food
		:parameters (?r ?f)
		:precondition 	(and (restaurant ?r) (food ?f) (me ?r)
						(waiting ?r) (has_food ?r ?f))
		:effect (and (not (waiting ?r))
				     (ready_at_food ?r ?f)
				)
	)	

	(:action go_to
		:parameters (?b ?dep ?dest)
		:precondition 	(and (delivery_boy ?b) (location ?dep) (location ?dest)
						(at ?dep ?b) (next_to ?dep ?dest))
		:effect 	(and (not (at ?dep ?b)) 
					(at ?dest ?b) 
				)
	)

		
	(:action pickup_food
		:parameters (?b ?f ?r)
		:precondition 	(and (delivery_boy ?b) (food ?f) (restaurant ?r)
						(at ?r ?b) (free ?b) (ready_at_food ?r ?f)) 
		:effect 	(and (waiting ?r)
				    (not (ready_at_food ?r ?f))
				    (with_food ?b ?f)
				    (not (free ?b))
				    (at ?r ?b)
				)
	)
	
	(:action deliver_food
		:parameters (?b ?f ?c)
		:precondition 	(and (delivery_boy ?b) (food ?f) (client ?c)
						(not (free ?b)) (at ?c ?b) (with_food ?b ?f)) 
		:effect 	(and (free ?b)
				    (served ?c ?f)
				    (not (with_food ?b ?f))
				)
	)


)
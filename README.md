# Loan-Service

A REST service that enables :

- loan preparators to send loan contracts to loan managers for approval.
- managers to give their decision on a request 
- managers to retrieve all requests assigned to him, that aer in pending status
- extract statistics on requests approved during some period of time (60 seconds by default)

Customer ID must have format XX-XXXX-XXX where X is either number or a letter, a customer may have only one pending request at a time

## Testing

**@PostMapping("/api/requests") - adding a new request**

![image](https://user-images.githubusercontent.com/90723839/166142871-dfc2e198-26cf-4349-a58c-ac4f6264ff44.png)

Let's try to add another request for the same customer:

![image](https://user-images.githubusercontent.com/90723839/166142897-1a044646-3c2e-4dba-af9c-66104fada1ed.png)

**@GetMapping("/api/{manager}/pending") - getting all requests pending for a manager (JOHN_JOHNSON)**

![image](https://user-images.githubusercontent.com/90723839/166143012-22a2808f-3efc-4688-9ff5-56d0da68caac.png)

**@PutMapping("/api/{id}/{customerID}/{manager}") - updating a decision on a request (request id: 1, customer id: 11-117L-PPL, manager: JOHN_JOHNSON)**

![image](https://user-images.githubusercontent.com/90723839/166143102-e9f4221d-31e5-40f1-ba99-0c0d250778dc.png)

**@GetMapping("/api/statistics") - getting statistics**

![image](https://user-images.githubusercontent.com/90723839/166143219-b206c434-405e-48b5-90ae-acc6dd83a0ab.png)

All statistics are 0, because ANGELA WHITE has not geven her approval on the request.

After approval by ANGELA WHITE:

![image](https://user-images.githubusercontent.com/90723839/166143297-2e489941-b8d6-4a73-a49e-d26002506b8a.png)

# Car Pooling Service Challenge

Design/implement a system to manage car pooling.

We provide the service of taking people from point A to point B.
So far we have done it without sharing cars with multiple groups of people.
This is an opportunity to optimize the use of resources by introducing car
pooling.

You have been assigned to build the car availability service that will be used
to track the available seats in cars.

Cars have a different amount of seats available, they can accommodate groups of
up to 4, 5 or 6 people.

People requests cars in groups of 1 to 6. People in the same group want to ride
on the same car. You can take any group at any car that has enough empty seats
for them. If it's not possible to accommodate them, they're willing to wait until 
there's a car available for them. Once a car is available for a group
that is waiting, they should ride. 

Once they get a car assigned, they will journey until the drop off, you cannot
ask them to take another car (i.e. you cannot swap them to another car to
make space for another group).

In terms of fairness of trip order: groups should be served as fast as possible,
but the arrival order should be kept when possible.
If group B arrives later than group A, it can only be served before group A
if no car can serve group A.

For example: a group of 6 is waiting for a car and there are 4 empty seats at
a car for 6; if a group of 2 requests a car you may take them in the car.
This may mean that the group of 6 waits a long time,
possibly until they become frustrated and leave.

## Evaluation rules

This challenge has a partially automated scoring system. This means that before
it is seen by the evaluators, it needs to pass a series of automated checks
and scoring.

### Checks

All checks need to pass in order for the challenge to be reviewed.

- The `acceptance` test step in the `.gitlab-ci.yml` must pass in master before you
submit your solution. We will not accept any solutions that do not pass or omit
this step. This is a public check that can be used to assert that other tests 
will run successfully on your solution. **This step needs to run without 
modification**
- _"further tests"_ will be used to prove that the solution works correctly. 
These are not visible to you as a candidate and will be run once you submit 
the solution

### Scoring

There is a number of scoring systems being run on your solution after it is 
submitted. It is ok if these do not pass, but they add information for the
reviewers.

## API

To simplify the challenge and remove language restrictions, this service must
provide a REST API which will be used to interact with it.

This API must comply with the following contract:

### GET /status

Indicate the service has started up correctly and is ready to accept requests.

Responses:

* **200 OK** When the service is ready to receive requests.

### PUT /cars

Load the list of available cars in the service and remove all previous data
(reset the application state). This method may be called more than once during
the life cycle of the service.

**Body** _required_ The list of cars to load.

**Content Type** `application/json`

Sample:

```json
[
  {
    "id": 1,
    "seats": 4
  },
  {
    "id": 2,
    "seats": 6
  }
]
```

Responses:

* **200 OK** When the list is registered correctly.
* **400 Bad Request** When there is a failure in the request format, expected
  headers, or the payload can't be unmarshalled.

### POST /journey

A group of people requests to perform a journey.

**Body** _required_ The group of people that wants to perform the journey

**Content Type** `application/json`

Sample:

```json
{
  "id": 1,
  "people": 4
}
```

Responses:

* **200 OK** or **202 Accepted** When the group is registered correctly
* **400 Bad Request** When there is a failure in the request format or the
  payload can't be unmarshalled.

### POST /dropoff

A group of people requests to be dropped off. Whether they traveled or not.

**Body** _required_ A form with the group ID, such that `ID=X`

**Content Type** `application/x-www-form-urlencoded`

Responses:

* **200 OK** or **204 No Content** When the group is unregistered correctly.
* **404 Not Found** When the group is not to be found.
* **400 Bad Request** When there is a failure in the request format or the
  payload can't be unmarshalled.

### POST /locate

Given a group ID such that `ID=X`, return the car the group is traveling
with, or no car if they are still waiting to be served.

**Body** _required_ A url encoded form with the group ID such that `ID=X`

**Content Type** `application/x-www-form-urlencoded`

**Accept** `application/json`

Responses:

* **200 OK** With the car as the payload when the group is assigned to a car. See below for the expected car representation 
```json
  {
    "id": 1,
    "seats": 4
  }
```

* **204 No Content** When the group is waiting to be assigned to a car.
* **404 Not Found** When the group is not to be found.
* **400 Bad Request** When there is a failure in the request format or the
  payload can't be unmarshalled.

## Tooling

We use Gitlab and Gitlab CI for our backend development work. 
In this repo you may find a [.gitlab-ci.yml](./.gitlab-ci.yml) file which
contains some tooling that would simplify the setup and testing of the
deliverable. This testing can be enabled by simply uncommenting the final
acceptance stage. Note that the image build should be reproducible within
the CI environment.

Additionally, you will find a basic Dockerfile which you could use a
baseline, be sure to modify it as much as needed, but keep the exposed port
as is to simplify the testing.

:warning: Avoid dependencies and tools that would require changes to the 
`acceptance` step of [.gitlab-ci.yml](./.gitlab-ci.yml), such as 
`docker-compose`

:warning: The challenge needs to be self-contained so we can evaluate it. 
If the language you are using has limitations that block you from solving this 
challenge without using a database, please document your reasoning in the 
readme and use an embedded one such as sqlite.

You are free to use whatever programming language you deem is best to solve the
problem but please bear in mind we want to see your best!

## Requirements

- The service should be as efficient as possible.
  It should be able to work reasonably well with at least $`10^4`$ / $`10^5`$ cars / waiting groups.
  Explain how you did achieve this requirement.
- You are free to modify the repository as much as necessary to include or remove
  dependencies, subject to tooling limitations above.
- Document your decisions using MRs or in this very README adding sections to it,
  the same way you would be generating documentation for any other deliverable.
  We want to see how you operate in a quasi real work environment.

# Solution

## Data structures
The following describes what decisions have been made focusing on the data structures taking into account the use cases raised in the challenge in order to maximize the performance of the application.

### Car data management
Two data structures have been used to manage cars data:
  - A HashMap to save all the cars indexed by car id (`loadedCars`)
  - A HashMap to save the available seats as key and a HasSet as value to save the cars that have the number of available seats defined in the key (`loadedCarIdsByFreeSeats`)

In one hand, `loadedCars` is useful to search a car by id in O(1) time complexity used from locate group of people and drop off use cases.

On the other hand, `loadedCarIdsByFreeSeats` is used to assign a car to a group of people as the main factor to select the car is the available free seats at the moment. This data structure allows to search the target car based on free seats count in O(1) time complexity.

### Group of people data management

Three data structures have been used to manage cars data:
  - A HashMap to save all the cars indexed by car id (`groupsOfPeopleById`)
  - A LinkedHashSet to save groups of people who are waiting for a car (`groupsOfPeopleQueue`)
  - A HashMap to save assigned car ids of groups of people (`carIdByGroupOfPeopleId`)

Firstly, `groupsOfPeopleById` is used to search a group of people by id in O(1) time complexity used from locate, add and drop off group of people use cases to check the existence of a group of people using the id.

Moreover, `groupsOfPeopleQueue` is used to store the groups of people respecting the order in which they were added, mainly used to assign a group of people to a car with enough available seats. The time complexity if this operation is O(N). The time complexity to add and remove elements is O(1).

Finally, `carIdByGroupOfPeopleId` is used to determine which car id has been assigned to a group of people id in O(1) time complexity. Used from locate use case, and drop off to check if the group of people has any car assigned.

## Data volume requirements
The challenge asks that the application must be able to manage 10^4 - 10^5 cars and groups of people reasonably efficiently. The data structures mentioned in the previous section have been designed to achieve the max efficiency and achieve this goal as a result in average:

In any case, in order to check if that goal has been achieved, an specific test has been added in `ControllerTest` that executes each following operations concurrently with this results:

**10^4 elements**
  - Load 10^4 cars: *38 milliseconds*
  - Adds 10^4 groups of people: *192 milliseconds*
  - Drops off previously created groups of people (10^4): *151 milliseconds*

**10^5 elements**
  - Load 10^5 cars: *121 milliseconds*
  - Adds 10^5 groups of people: *4289 milliseconds*
  - Drops off previously created groups of people (10^5): *3897 milliseconds*
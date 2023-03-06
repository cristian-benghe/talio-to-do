# Code Contributions and Code Reviews


#### Focused Commits

Grade: Excellent

Feedback: for the amount of work done there is a good amount of commits. Commits meaningful and of reasonable size. Commit messages concise and clear, and use the same format.

#### Isolation

Grade: Good

Feedback: Use feature branches, but use their names as branch names. It's better to use the issue the branch is created for as a branch name. I would also prefer if you linked branches to issues (go to a specific issue then the blue button on the right "Create merge request" >> dropdown >> create branch) you can also mention in the MR which issue it closes (`closes !XX`).


#### Reviewability

Grade: Good

Feedback: MRs are focused, and some of them have a description of the change (make sure all of them have one, you can also add a template on GitLab to make it easier). Same as with branches, MRs should not be named after the author but relate to the issue that it resolves.


#### Code Reviews

Grade: Sufficient

Feedback: review is done timely, no/few comments usually only saying that everything looks good (fine for small MRs), no discussions, only one approver (should be 2), example: !46


#### Build Server

Grade: Insufficient

Feedback: That is completely unacceptable to have a failing pipeline on main. The pipeline should **always** pass and if it fails it has to be fixed right away. MR that have a failing pipeline should never be approved or merged.


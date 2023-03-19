# Code Contributions and Code Reviews

I noticed that some team members are not contributing to the code base (I'm talking about java files). I have to remind you that even when working on the front end you still have to code (controllers etc.), conributing only to the scenes is not sufficient to meet the code contribution criterium.

#### Focused Commits

Grade: Sufficient

Feedback: Commits are of reasonable size, and messages follow the convention. Try to avoid commits with meaningless changes and be more descriptive in the messages (for example these 2 commits: [1](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-54/-/commit/c7b7e540cb67359d7eba433f9ea7d1babcf3c3ce), [2](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-54/-/commit/5d4791372d8e4fc9465501e075b33b4e47bfdd76) have the same message, but one of them is just deleting unused imports). It doesn't happen often, but be careful about it.


#### Isolation

Grade: Good

Feedback: Use feature branches. The team has a lot of MRs, try to avoid MRs that have only a few lines of code (!96)


#### Reviewability

Grade: Very Good

Feedback: MRs contain an additional description and usually link to specific issues (counter-example - !81). Cover a small number of commits and are coherent.


#### Code Reviews

Grade: Sufficient

Feedback: Every MR is reviewed, and some suggestions are made. However, I would like to see more discussions happening in the comments and see that the reviews are actually leading to code improvements. For example, if you comment that there should be more tests, then the author of the MR should update the branch with more tests (!98).


#### Build Server

Grade: Insufficient

Feedback: Build fails rarely and is fixed right away. Still no checkstyle on main (that's why insufficient)


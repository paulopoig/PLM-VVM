# PLM-VVM
Attempt on our Application Development Final Project
Just download the main folder and open it on Android Studio and you are good to go!



# Workflow

1. Go to ``main`` branch
```vim
git checkout final
```
  - You can also do this in **Github Desktop** by clicking on **Branch dropdown** and clicking on ``final`` branch.

2. Clean your banch from outside changes. **Note: This will discard any and all file changes**.
```vim
git reset --hard
```
  - Alternatively, on **Github Desktop :** highlight all files on the **Left side**, right-click, and select ``Discard <number of files> selected changes...``

3. **Pull** updates from **origin**.
```vim
git pull
```
  - Alternatively, on **Github Desktop :** click on ``Fetch origin``. If there is an update, click on ``Pull origin``.

4. Create **your own branch** from ``final`` branch as base. <br>
Replace ``<branchname>`` with the name of **your choosing (example: your surname)**.
```vim
git checkout -b <branchname>
```
```vim
git push origin <branchname>
```
  - Alternatively, on **Github Desktop :** click on the **Branches Dropdown**, and clicking on ``New branch``.
  - Make sure to click on ``Publish Branch`` to upload the branch to main repository.

5. **Make your changes to the files**.
  - Write codes, edit codes, etc.
  
6. Use **Github Desktop** to **commit your file changes**.

7. Click on ``Push origin``, to upload changes to main repository.

8. Make a **PR (Pull Request)** to integrate changes to ``final`` branch.
  - Do this via the **Github repository link**., **OR** message Repo owner to do the PR.
  - Wait for the PR to be approved and merged.
  
9. Delete **your local branch**. **Note: This is OK to do since all the changes have been uploaded already)**. <br>
Replace ``<branchname>`` with the name of the branch from ``Step #4``.
```vim
git branch -d <branchname>
```

10. Go back to ``Step #1``.

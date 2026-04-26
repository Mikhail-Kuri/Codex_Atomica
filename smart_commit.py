import os
import sys
import subprocess

def check_quit(value):
    if value.strip().lower() == "q":
        print("\n👋 Quitter le script...")
        sys.exit(0)

def get_current_branch():
    return subprocess.check_output(
        "git rev-parse --abbrev-ref HEAD",
        shell=True
    ).decode().strip()

def has_upstream():
    try:
        subprocess.check_output(
            "git rev-parse --abbrev-ref --symbolic-full-name @{u}",
            shell=True,
            stderr=subprocess.DEVNULL
        )
        return True
    except subprocess.CalledProcessError:
        return False

# ---------------------------
# 1. TYPE MENU
# ---------------------------
print("\nChoose commit type:")
print("[f] feature")
print("[x] fix")
print("[r] refactor")
print("[d] docs")
print("[c] chore")
print("[q] quit")

choice = input("\nType (letter): ").strip().lower()
check_quit(choice)

types = {
    "f": "feature",
    "x": "fix",
    "r": "refactor",
    "d": "docs",
    "c": "chore"
}

commit_type = types.get(choice, "chore")

# ---------------------------
# 2. DESCRIPTION
# ---------------------------
description = input("Description: ").strip()
check_quit(description)

# ---------------------------
# 3. PUSH ?
# ---------------------------
push_choice = input("Push? (y/n): ").strip().lower()
check_quit(push_choice)

# ---------------------------
# 4. MESSAGE
# ---------------------------
commit_message = f"{commit_type}: {description}"

# ---------------------------
# 5. GIT ADD + COMMIT
# ---------------------------
print("\n📦 staging...")
os.system("git add -A")

print("💬 committing...")
os.system(f'git commit -m "{commit_message}"')

# ---------------------------
# 6. PUSH OPTIONAL
# ---------------------------
if push_choice == "y":
    branch = get_current_branch()

    if has_upstream():
        print("🚀 pushing...")
        os.system("git push")
    else:
        print(f"🚀 first push → setting upstream to origin/{branch}...")
        os.system(f"git push --set-upstream origin {branch}")

    print("✅ pushed!")
else:
    print("⏸️ no push")

# ---------------------------
# 7. DONE
# ---------------------------
print("\n🔥 DONE")
print(commit_message)
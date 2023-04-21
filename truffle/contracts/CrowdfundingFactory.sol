//SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.8.0;

import "./Crowdfunding.sol";

contract CrowdfundingFactory is Ownable {
    mapping(address => ProjectDetails) public projects;
    mapping(address => bool) public allowedAddresses;
    bool isFreeForAll;

    struct ProjectDetails {
        string description;
        Crowdfunding project;
    }

    event NewProjectInitiated(
        address _projectAddress,
        string _description,
        uint _goal,
        uint _timeAhead,
        address _tokenUsed
    );
    event ContributeEvent(
        address _projectAddress,
        address _sender,
        address _tokenAddress,
        uint _amount
    );
    event RefundFromProject(
        address _projectAddress,
        address _contributer,
        address _RefundToken,
        uint _RefundAmount
    );
    event CreateRequestEvent(
        address _projectAddress,
        string _description,
        address _recipient,
        uint _amount
    );
    event ReceiveContributionEvent(
        address _projectAddress,
        address _recipient,
        uint _amount
    );

    constructor() {
        isFreeForAll = true;
    }

    function createNewProject(
        uint _goal,
        uint _timeAhead,
        address _acceptingThisToken,
        string memory _description
    ) public returns (address) {
        if (!isFreeForAll) {
            require(
                allowedAddresses[msg.sender] == true,
                "Address is not allowed"
            );
        }

        Crowdfunding crowdfunding = new Crowdfunding(
            address(this),
            _goal,
            _timeAhead,
            _acceptingThisToken
        );

        require(address(crowdfunding) != address(0));
        crowdfunding.transferOwnership(msg.sender);

        ProjectDetails storage newProject = projects[address(crowdfunding)];

        newProject.description = _description;
        newProject.project = crowdfunding;

        emit NewProjectInitiated(
            address(crowdfunding),
            _description,
            _goal,
            _timeAhead,
            _acceptingThisToken
        );
        address addy = address(crowdfunding);
        return addy;
    }

    function contributeToProject(address _projectAddress, uint _amount) public {
        Crowdfunding project = Crowdfunding(_projectAddress);
        address _contributer;
        address _tokenAddress = project.tokenAddress();

        (_contributer) = project.contribute(_amount, msg.sender);
        emit ContributeEvent(
            _projectAddress,
            _contributer,
            _tokenAddress,
            _amount
        );
    }

    function getBalanceOfProject(
        address _projectAddress
    ) public view returns (uint) {
        Crowdfunding project = Crowdfunding(_projectAddress);
        return project.getBalance();
    }

    function getRefundFromProject(
        address _projectAddress
    ) public returns (uint) {
        Crowdfunding project = Crowdfunding(_projectAddress);
        uint _RefundAmount = project.getRefund(msg.sender);
        emit RefundFromProject(
            _projectAddress,
            msg.sender,
            project.tokenAddress(),
            _RefundAmount
        );
        return _RefundAmount;
    }

    function createRequestForProject(
        address _projectAddress,
        string memory _description,
        address _recipient,
        uint _amount
    ) public {
        Crowdfunding project = Crowdfunding(_projectAddress);
        project.createRequest(_description, _recipient, _amount);

        emit CreateRequestEvent(
            _projectAddress,
            _description,
            _recipient,
            _amount
        );
    }

    function voteRequestForProject(
        address _projectAddress,
        uint _requestNo
    ) public {
        Crowdfunding project = Crowdfunding(_projectAddress);
        project.voteRequest(_requestNo, msg.sender);
    }

    function receiveContributionFromProject(
        address _projectAddress,
        uint _requestNo
    ) public {
        Crowdfunding project = Crowdfunding(_projectAddress);
        address _recipient;
        uint _amount;
        (_recipient, _amount) = project.receiveContribution(_requestNo);

        emit ReceiveContributionEvent(_projectAddress, _recipient, _amount);
    }

    // function getContributerAmountOfProject(
    //     address _projectAddress
    // ) public view returns (uint) {
    //     Crowdfunding project = Crowdfunding(_projectAddress);
    //     return project.contributers(msg.sender);
    // }

    // function getDeadlineOfProject(
    //     address _projectAddress
    // ) public view returns (uint) {
    //     Crowdfunding project = Crowdfunding(_projectAddress);
    //     return project.deadline();
    // }

    function allowAddress(address _allowThisAddress) public onlyOwner {
        require(allowedAddresses[_allowThisAddress] == false);
        allowedAddresses[_allowThisAddress] = true;
    }

    function disallowAddress(address _disallowThisAddress) public onlyOwner {
        require(allowedAddresses[_disallowThisAddress] == true);
        allowedAddresses[_disallowThisAddress] = false;
    }

    function getRequestOfProject(
        address _projectAddress,
        uint _requestNo
    ) public view returns (string memory, address, uint, bool, uint, uint) {
        Crowdfunding project = Crowdfunding(_projectAddress);
        return (
            project.getRequestDescription(_requestNo),
            project.getRequestRecipient(_requestNo),
            project.getRequestAmount(_requestNo),
            project.getRequestCompleted(_requestNo),
            project.getRequestNumOfVoters(_requestNo),
            project.getRequestValueOfVotes(_requestNo)
        );
    }

    function getNumOfRequestOfProject(
        address _projectAddress
    ) public view returns (uint) {
        Crowdfunding project = Crowdfunding(_projectAddress);
        return project.numRequests();
    }

    function getDescriptionOfProject(
        address _projectAddress
    ) public view returns (string memory, address) {
        string memory d = projects[_projectAddress].description;
        Crowdfunding a = projects[_projectAddress].project;
        return (d, address(a));
    }

    function toggleFreeForAll() public {
        isFreeForAll = !isFreeForAll;
    }
}
